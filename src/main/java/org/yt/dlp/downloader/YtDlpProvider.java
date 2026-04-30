package org.yt.dlp.downloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;

public class YtDlpProvider {

    private static Path ytDlpPath;
    private static Path ffmpegPath;
    private static Path ffprobePath;

    public static synchronized Path getYtDlp() throws IOException {
        // If the file exists, re-use it
        if (ytDlpPath != null && Files.exists(ytDlpPath)) {
            return ytDlpPath;
        }

        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindowsOs = os.contains("win");
        boolean isLinuxOs = os.contains("linux");
        boolean isMacOs = os.contains("mac");

        String resourceName;
        String fileName;
        Path cachePath;

        if(isWindowsOs){
            resourceName = "yt-dlp/windows/yt-dlp.exe";
            fileName = "yt-dlp.exe";
            cachePath = Paths.get(
                    System.getenv("LOCALAPPDATA"),
                    "yt-dlp-downloader"
            );
            dependencyProvider("win");
        }
        else if (isLinuxOs) {
            resourceName = "yt-dlp/linux/yt-dlp";
            fileName = "yt-dlp";
            cachePath = Paths.get(
                    System.getProperty("user.home"),
                    ".cache",
                    "yt-dlp-downloader"
            );
            dependencyProvider("linux");
        }
        else if (isMacOs) {
            resourceName = "yt-dlp/mac/yt-dlp_macos";
            fileName = "yt-dlp_macos";
            cachePath = Paths.get(
                    System.getProperty("user.home"),
                    "library",
                    "Caches",
                    "yt-dlp-downloader"
            );
            dependencyProvider("mac");
        }
        else {
            throw new RuntimeException("Operating system not supported");
        }

        // If it does not exist, create the file
        Files.createDirectories(cachePath);
        ytDlpPath = cachePath.resolve(fileName);


        if (!Files.exists(ytDlpPath)) {

            try (InputStream is = YtDlpProvider.class.getClassLoader().getResourceAsStream(resourceName)) {
                if (is == null) {
                    throw new FileNotFoundException("Resource yt-dlp not found.");
                }
                Files.copy(is, ytDlpPath, StandardCopyOption.REPLACE_EXISTING);
            }

            // Linux and MacOs permissions
            if (!isWindowsOs){
                ytDlpPath.toFile().setExecutable(true);
            }
        }

        return ytDlpPath;
    }

    static void dependencyProvider(String os) throws IOException {

        Path cachePath;

        String ffmpegResName;
        String ffprobeResName;

        String ffmpegFileName;
        String ffprobeFileName;

        if (ffmpegPath != null && Files.exists(ffmpegPath) && ffprobePath != null && Files.exists(ffprobePath)) {
            return;
        }

        if (os.contains("win")){
            ffmpegResName = "ffmpeg/windows/ffmpeg.exe";
            ffprobeResName = "ffmpeg/windows/ffprobe.exe";

            ffmpegFileName = "ffmpeg.exe";
            ffprobeFileName = "ffprobe.exe";

            cachePath = Paths.get(
                    System.getenv("LOCALAPPDATA"),
                    "yt-dlp-downloader",
                    "bin"
            );
        }
        else if (os.contains("linux")) {
            ffmpegResName = "ffmpeg/linux/ffmpeg";
            ffprobeResName = "ffmpeg/linux/ffprobe";

            ffmpegFileName = "ffmpeg";
            ffprobeFileName = "ffprobe";

            cachePath = Paths.get(
                    System.getProperty("user.home"),
                    ".cache",
                    "yt-dlp-downloader",
                    "bin"
            );
        }
        else if (os.contains("mac")) {
            ffmpegResName = "ffmpeg/mac/ffmpeg_macos";
            ffprobeResName = "ffmpeg/mac/ffprobe_macos";

            ffmpegFileName = "ffmpeg_macos";
            ffprobeFileName = "ffprobe_macos";

            cachePath = Paths.get(
                    System.getProperty("user.home"),
                    "library",
                    "Caches",
                    "yt-dlp-downloader",
                    "bin"
            );
        }
        else {
            throw new RuntimeException("OS not supported");
        }

        Files.createDirectories(cachePath);
        ffmpegPath = cachePath.resolve(ffmpegFileName);
        ffprobePath = cachePath.resolve(ffprobeFileName);

        if (!Files.exists(ffmpegPath) && !Files.exists(ffprobePath)) {

            try (InputStream inputStream = YtDlpProvider.class.getResourceAsStream("/" + ffmpegResName)) {
                if (inputStream == null) {
                    throw new FileNotFoundException("Resource ffmpeg not found");
                }
                Files.copy(inputStream, ffmpegPath, StandardCopyOption.REPLACE_EXISTING);
            }
            try (InputStream inputStream = YtDlpProvider.class.getResourceAsStream("/" + ffprobeResName)) {
                if (inputStream == null) {
                    throw new FileNotFoundException("Resource ffprobe not found");
                }
                Files.copy(inputStream, ffprobePath, StandardCopyOption.REPLACE_EXISTING);
            }

            if (!os.contains("win")) {
                ffmpegPath.toFile().setExecutable(true);
                ffprobePath.toFile().setExecutable(true);
            }

        }
    }
}
