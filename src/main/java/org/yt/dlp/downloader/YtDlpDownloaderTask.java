package org.yt.dlp.downloader;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class YtDlpDownloaderTask extends Task<Void> {

    private final String videoUrl;
    private final String downloadType;
    private final Path ytDlp;
    private final File downloadFolder;

    public YtDlpDownloaderTask(String videoUrl, String downloadType, File downloadFolder) throws IOException {
        this.videoUrl = videoUrl;
        this.downloadType = downloadType;
        this.downloadFolder = downloadFolder;
        this.ytDlp = YtDlpProvider.getYtDlp();
    }

    @Override
    protected Void call() throws Exception {


//        File videoDirectory = new File("videos");
//        if (!videoDirectory.exists()) {
//            videoDirectory.mkdirs();
//        }

        File videoDirectory;
        if (downloadFolder != null) {
            videoDirectory = downloadFolder;
        }
        else {
            videoDirectory = new File(System.getProperty("user.home") + "/Videos");
        }

        String outputTemplate = new File(videoDirectory, "%(title)s.%(ext)s").getAbsolutePath();

        ProcessBuilder ytDlpProcess = new ProcessBuilder();

        if(downloadType.equals("mp3")) {
            ytDlpProcess.command(
                    ytDlp.toAbsolutePath().toString(),
                    "-x", "--audio-format", downloadType,
                    "--progress",
                    "-o", outputTemplate,
                    videoUrl
            );
        }
        else {
            ytDlpProcess.command(
                    ytDlp.toAbsolutePath().toString(),
                    "-f", "bv*[vcodec^=avc1]+ba[acodec^=mp4a]/b",
                    "--merge-output-format", downloadType,
                    "--progress",
                    "-o", outputTemplate,
                    videoUrl
            );
        }

        ytDlpProcess.redirectErrorStream(true);
        Process process = ytDlpProcess.start();

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {

                // yt-dlp imprime progresso assim:
                // [download]  42.3% of ...
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
        if(exitCode != 0) {
            throw new RuntimeException("Download error");
        }

        updateProgress(1, 1);
        updateMessage("Done!");
        return null;
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
}
