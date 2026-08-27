package org.yt.dlp.downloader;

import javafx.concurrent.Task;
import org.yt.dlp.util.DependCheck;
import org.yt.dlp.util.OsCheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class YtDlpDownloaderTask extends Task<Void> {

    private final String videoUrl;
    private final String downloadType;
    private final File downloadFolder;

    public YtDlpDownloaderTask(String videoUrl, String downloadType, File downloadFolder) throws IOException {
        this.videoUrl = videoUrl;
        this.downloadType = downloadType;
        this.downloadFolder = downloadFolder;
        //this.ytDlp = YtDlpProvider.getYtDlp();
    }

    @Override
    protected Void call() throws Exception {

//        String os = System.getProperty("os.name").toLowerCase();
//        boolean isWindowsOs = os.contains("win");
//        boolean isLinuxOs = os.contains("linux");
//        boolean isMacOs = os.contains("mac");
//        boolean isFmpegInstalled;
//
//        String ytDlpPath;
//
//        if(DependCheck.ytDlpCheck()){
//            ytDlpPath = "yt-dlp";
//        }
//        else{
//            ytDlpPath = DependencyProvider.getDependecies();
//        }
//
//        isFmpegInstalled = DependCheck.ffmpegCheck();
//
//        if(!isFmpegInstalled){
//            if (isWindowsOs) {
//                ffmpegPath = Paths.get(
//                        System.getenv("LOCALAPPDATA"),
//                        "yt-dlp-downloader"
//                ).toString();
//            }
//            else if (isLinuxOs) {
//                ffmpegPath = Paths.get(
//                        System.getProperty("user.home"),
//                        ".cache",
//                        "yt-dlp-downloader"
//                ).toString();
//            }
//            else if (isMacOs) {
//                ffmpegPath = Paths.get(
//                        System.getProperty("user.home"),
//                        "library",
//                        "Caches",
//                        "yt-dlp-downloader"
//                ).toString();
//            }
//            else {
//                throw new RuntimeException("OS not supported");
//            }
//        }

        File videoDirectory;
        if (downloadFolder != null) {
            videoDirectory = downloadFolder;
        }
        else {
            videoDirectory = new File(System.getProperty("user.home") + "/Videos");
        }

        Process process = getProcess(videoDirectory);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {

                updateMessage(line);

                if (line.contains("%")) {
                    parseProgress(line);
                }

                if (isCancelled()) {
                    process.destroyForcibly();
                    break;
                }
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("Download error");
        }

        updateProgress(1, 1);
        updateMessage("Done!");
        return null;
    }

    private Process getProcess(File videoDirectory) throws IOException {
        ArrayList<String> command = getCommand(videoDirectory);
        ProcessBuilder ytDlpProcess = new ProcessBuilder();

        ytDlpProcess.command(command);

//        if(downloadType.equals("mp3")) {
//            ytDlpProcess.command(
//                    ytDlpPath,
//                    "--ffmpeg-location", ffmpegPath,
//                    "-x", "--audio-format", downloadType,
//                    "--progress",
//                    "-o", outputTemplate,
//                    videoUrl
//            );
//        }
//        else {
//            ytDlpProcess.command(
//                    ytDlpPath,
//                    "--ffmpeg-location", ffmpegPath,
//                    "-f", "bv*[vcodec^=avc1]+ba[acodec^=mp4a]/b",
//                    "--merge-output-format", downloadType,
//                    "--progress",
//                    "-o", outputTemplate,
//                    videoUrl
//            );
//        }

        ytDlpProcess.redirectErrorStream(true);
        return ytDlpProcess.start();
    }

    protected void canceled() {
        super.cancelled();
        updateMessage("Download cancelled");
    }

    private void parseProgress(String line) {
        try {
            int percentIndex = line.indexOf('%');
            int start = line.lastIndexOf(' ', percentIndex);
            double percent = Double.parseDouble(
                    line.substring(start, percentIndex).trim()
            );
            updateProgress(percent, 100);
            updateMessage("Downloading... " + percent + "%");
        } catch (Exception ignored) {}
    }

    private ArrayList<String> getCommand(File videoDirectory) throws IOException {
        String outputTemplate = new File(videoDirectory, "%(title)s.%(ext)s").getAbsolutePath();
        ArrayList<String> command = new ArrayList<>();

        boolean isFmpegInstalled = DependCheck.ffmpegCheck();
        boolean isYtdlpInstalled = DependCheck.ytDlpCheck();

        if (isYtdlpInstalled){
            command.add("yt-dlp");
        }
        else {
            command.add(DependencyProvider.getDependecies());
        }

        if(!isFmpegInstalled){
            command.add("--ffmpeg-location");
            command.add(getFfmpegPath());
        }

        if(downloadType.equals("mp3")){
            command.add("-x");
            command.add("--audio-format");
        }
        else {
            command.add("-f");
            command.add("bv*[vcodec^=avc1]+ba[acodec^=mp4a]/b");
            command.add("--merge-output-format");
        }

        command.add(downloadType);
        command.add("--progress");
        command.add("-o");
        command.add(outputTemplate);
        command.add(videoUrl);

        return command;
    }

    private String getFfmpegPath(){
        OsCheck.OS systemOs = OsCheck.getSystemOs();

        switch (systemOs){
            case WIN:
                return Paths.get(
                    System.getenv("LOCALAPPDATA"),
                    "yt-dlp-downloader"
            ).toString();

            case MAC:
                return Paths.get(
                    System.getProperty("user.home"),
                    "library",
                    "Caches",
                    "yt-dlp-downloader"
            ).toString();

            case LIN:
                return Paths.get(
                    System.getProperty("user.home"),
                    ".cache",
                    "yt-dlp-downloader"
            ).toString();

            default:
                throw new RuntimeException("OS not supported");
        }
    }
}
