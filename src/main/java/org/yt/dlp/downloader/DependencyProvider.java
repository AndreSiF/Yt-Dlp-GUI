package org.yt.dlp.downloader;

import org.yt.dlp.util.DependCheck;
import org.yt.dlp.util.OsCheck;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipFile;

public class DependencyProvider {
    private static Path cachePath;

    private static final String YTDLP_BASE = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/";
    private static final String FFBINARIES_BASE = "https://github.com/ffbinaries/ffbinaries-prebuilt/releases/download/v4.4.1/";

    public static synchronized String getDependecies() throws IOException {
        boolean isFfmpegInstalled = DependCheck.ffmpegCheck();

        // Verify what OS is running and store it
        OsCheck.OS systemOS = OsCheck.getSystemOs();

        // Set the cache path for files before downloading
        setCachePath(systemOS);

        if(!isFfmpegInstalled){
            getFfmpeg(systemOS);
        }
        return getYtDlp(systemOS);
    }

    public static void getFfmpeg(OsCheck.OS os) throws IOException {
        String urlFfmpeg;
        String urlFfprobe;
        String ffmpegBinaryName;
        String ffprobeBinaryName;

        if(os == OsCheck.OS.WIN){
            ffmpegBinaryName = "ffmpeg.exe";
            ffprobeBinaryName = "ffprobe.exe";
        }
        else{
            ffmpegBinaryName = "ffmpeg";
            ffprobeBinaryName = "ffprobe";
        }

        Path ffmpegBinaryPath = cachePath.resolve(ffmpegBinaryName);
        Path ffProbebinaryPath = cachePath.resolve(ffprobeBinaryName);
        if(Files.exists(ffmpegBinaryPath) || Files.exists(ffProbebinaryPath)){
            return;
        }

        switch (os){
            case WIN:
                urlFfmpeg = FFBINARIES_BASE + "ffmpeg-4.4.1-win-64.zip";
                urlFfprobe = FFBINARIES_BASE + "ffprobe-4.4.1-win-64.zip";
                break;
            case MAC:
                urlFfmpeg = FFBINARIES_BASE + "ffmpeg-4.4.1-osx-64.zip";
                urlFfprobe = FFBINARIES_BASE + "ffprobe-4.4.1-osx-64.zip";
                break;
            case LIN:
            default:
                urlFfmpeg = FFBINARIES_BASE + "ffmpeg-4.4.1-linux-64.zip";
                urlFfprobe = FFBINARIES_BASE + "ffprobe-4.4.1-linux-64.zip";
                break;
        }

        Path temp = cachePath.resolve("ffmpeg_temp.zip");

        downloadDependency(urlFfmpeg, temp);
        extractFileFromZip(temp, ffmpegBinaryName, ffmpegBinaryPath);
        Files.deleteIfExists(temp);

        downloadDependency(urlFfprobe, temp);
        extractFileFromZip(temp, ffprobeBinaryName, ffProbebinaryPath);
        Files.deleteIfExists(temp);

        if(os != OsCheck.OS.WIN){
            ffmpegBinaryPath.toFile().setExecutable(true);
            ffProbebinaryPath.toFile().setExecutable(true);
        }
    }

    private static String getYtDlp(OsCheck.OS os){
        String url;
        String ytDlpBinaryName;

        switch (os){
            case WIN:
                url = YTDLP_BASE + "yt-dlp.exe";
                ytDlpBinaryName = "yt-dlp.exe";
                break;

            case MAC:
                url = YTDLP_BASE + "yt-dlp_macos";
                ytDlpBinaryName = "yt-dlp";
                break;

            case LIN:
            default:
                url = YTDLP_BASE + "yt-dlp";
                ytDlpBinaryName = "yt-dlp";
                break;
        }

        // If the file existe, re-use it
        Path ytDlpBinaryPath = cachePath.resolve(ytDlpBinaryName);
        if(Files.exists(ytDlpBinaryPath)){
            return ytDlpBinaryPath.toString();
        }
        else {
            downloadDependency(url, ytDlpBinaryPath);
            if(os != OsCheck.OS.WIN){
                ytDlpBinaryPath.toFile().setExecutable(true);
            }
        }
        return ytDlpBinaryPath.toString();
    }

    private static void setCachePath(OsCheck.OS systemOS){

        switch (systemOS){
            case WIN -> cachePath = Paths.get(
                    System.getenv("LOCALAPPDATA"),
                    "yt-dlp-downloader"
            );
            case MAC -> cachePath = Paths.get(
                    System.getProperty("user.home"),
                    "library",
                    "Caches",
                    "yt-dlp-downloader"
            );
            case LIN -> cachePath = Paths.get(
                    System.getProperty("user.home"),
                    ".cache",
                    "yt-dlp-downloader"
            );
            default -> throw new RuntimeException("Operating system not supported");
        }
    }

    private static void downloadDependency(String url, Path destination){

        try {

            // Getting the file from the repository
            HttpClient httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();

            HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(url))
                    .header("User-Agent", "Java-Downloader").GET().build();

            HttpResponse<InputStream> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());

            // If "ok", create or replace file
            if(httpResponse.statusCode() == 200){
                InputStream inputStream = httpResponse.body();
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }
            else {
                throw new IOException("Download failed: HttpCode: " + httpResponse.statusCode());
            }

        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Simple extracting stuff from zip files function
    private static void extractFileFromZip(Path zipPath, String entryName, Path destination) throws IOException {
        try (ZipFile zipFile = new ZipFile(zipPath.toFile())) {
            var entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                var entry = entries.nextElement();
                if (entry.getName().endsWith(entryName)) {
                    try (InputStream is = zipFile.getInputStream(entry)) {
                        Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING);
                    }
                    return;
                }
            }
        }
        throw new IOException("Binary " + entryName + " not found on ZIP file.");
    }
}