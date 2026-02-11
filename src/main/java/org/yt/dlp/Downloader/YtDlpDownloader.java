package org.yt.dlp.Downloader;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class YtDlpDownloader {

    public static String DownloadVideo(String videoUrl, String downloadType) {
        try{
            Path ytDlp = YtDlpProvider.getYtDlp();

//          String videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

            // Path for videos
            File outputDir = new File("videos");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Template for the video output
            String outputTemplate = new File(outputDir, "%(title)s.%(ext)s").getAbsolutePath();

            // Download the file in using SwingWorker for no GUI lag
            new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    ProcessBuilder pb = new ProcessBuilder();
                    if(downloadType.equals("mp3")){
                        pb.command(
                                ytDlp.toAbsolutePath().toString(),
                                "-x", "--audio-format", downloadType,
                                "--restrict-filenames",
                                "-o", outputTemplate,
                                videoUrl);
                    }
                    else{
                        pb.command(
                                ytDlp.toAbsolutePath().toString(),
                                "-f", "bv*[vcodec^=avc1]+ba[acodec^=mp4a]/b",
                                "--merge-output-format", downloadType,
                                "--restrict-filenames",
                                "-o", outputTemplate,
                                videoUrl
                        );
                    }

                    // Show logs on console
                    pb.inheritIO();

                    System.out.println("Iniciando download com yt-dlp...");
                    Process process = pb.start();
                    int exitCode = process.waitFor();
                    System.out.println("yt-dlp finalizou com código: " + exitCode);
                    return null;
                }
            }.execute();
            return "Download concluido!";
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Erro ao executar yt-dlp.");
            return "Erro no download";
        }
    }

}
