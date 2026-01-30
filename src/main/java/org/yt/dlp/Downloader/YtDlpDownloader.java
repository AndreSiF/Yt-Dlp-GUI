package org.yt.dlp.Downloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class YtDlpDownloader {

    public String DownloadMp4(String videoUrl) {
        try{
            Path ytDlp = YtDlpProvider.getYtDlp();

//      String videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

            // Caminho onde o vídeo será salvo
            File outputDir = new File("videos");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Comando yt-dlp para baixar o vídeo com melhor qualidade
            String outputTemplate = new File(outputDir, "%(title)s.%(ext)s").getAbsolutePath();
            //bv*+ba[ext=m4a]/best[ext=mp4]
            ProcessBuilder pb = new ProcessBuilder(
                    //"C:\\yt-dlp\\yt-dlp.exe"
                    ytDlp.toAbsolutePath().toString(),
                    "-f", "bestvideo[ext=mp4]+bestaudio[ext=m4a]/best[ext=mp4]/best",
                    "--merge-output-format", "mp4",
                    "-o", outputTemplate,
                    videoUrl
            );

            // Usar a saída padrão do processo (mostrar logs no console)
            pb.inheritIO();

            System.out.println("Iniciando download com yt-dlp...");
            Process process = pb.start();
            int exitCode = process.waitFor();
            System.out.println("yt-dlp finalizou com código: " + exitCode);
            return "Download concluido!";
        }
        catch (InterruptedException | IOException e){
            e.printStackTrace();
            System.out.println("Erro ao executar yt-dlp.");
            return "Erro no download";
        }
    }

}
