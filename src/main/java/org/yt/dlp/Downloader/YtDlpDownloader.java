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

            // Caminho onde o vídeo será salvo
            File outputDir = new File("videos");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Comando yt-dlp para baixar o vídeo com melhor qualidade
            String outputTemplate = new File(outputDir, "%(title)s.%(ext)s").getAbsolutePath();

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


                    // Usar a saída padrão do processo (mostrar logs no console)
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
