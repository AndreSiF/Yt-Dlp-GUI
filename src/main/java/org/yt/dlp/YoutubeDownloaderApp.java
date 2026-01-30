package org.yt.dlp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class YoutubeDownloaderApp {

//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Digite o link do vídeo: ");
//        String videoUrl = sc.nextLine();
//        try {
//            Path ytDlp = YtDlpProvider.getYtDlp();
//
////      String videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
//
//            // Caminho onde o vídeo será salvo
//            File outputDir = new File("videos");
//            if (!outputDir.exists()) {
//                outputDir.mkdirs();
//            }
//
//            // Comando yt-dlp para baixar o vídeo com melhor qualidade
//            String outputTemplate = new File(outputDir, "%(title)s.%(ext)s").getAbsolutePath();
//
//            ProcessBuilder pb = new ProcessBuilder(
//                    //"C:\\yt-dlp\\yt-dlp.exe"
//                    ytDlp.toAbsolutePath().toString(),
//                    "-f", "bv*+ba[ext=m4a]/best[ext=mp4]",
//                    "--merge-output-format", "mp4",
//                    "-o", outputTemplate,
//                    videoUrl
//            );
//
//            // Usar a saída padrão do processo (mostrar logs no console)
//            pb.inheritIO();
//
//            System.out.println("Iniciando download com yt-dlp...");
//            Process process = pb.start();
//            int exitCode = process.waitFor();
//            System.out.println("yt-dlp finalizou com código: " + exitCode);
//        }
//        catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//            System.out.println("Erro ao executar yt-dlp.");
//        }
//    }
}

