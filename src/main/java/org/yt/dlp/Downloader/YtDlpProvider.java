package org.yt.dlp.Downloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class YtDlpProvider {
    private static final String RESOURCE_NAME = "yt-dlp";
    private static final String APP_CACHE_DIR =
            System.getProperty("user.home") + "/.cache/yt-dlp";

    private static Path ytDlpPath;

    public static synchronized Path getYtDlp() throws IOException {
        // Se o arquivo existir, reutiliza
        if (ytDlpPath != null && Files.exists(ytDlpPath)) {
            return ytDlpPath;
        }

        // Se nao existir, cria o arquivo
        Path cacheDir = Paths.get(APP_CACHE_DIR);
        Files.createDirectories(cacheDir);

        ytDlpPath = cacheDir.resolve("yt-dlp");

        if (!Files.exists(ytDlpPath)) {

            try (InputStream is = YtDlpProvider.class.getClassLoader().getResourceAsStream(RESOURCE_NAME)) {
                if (is == null) {
                    throw new FileNotFoundException("Resource yt-dlp não encontrado.");
                }
                Files.copy(is, ytDlpPath, StandardCopyOption.REPLACE_EXISTING);
            }

            // Permissão de execução (Linux)
            ytDlpPath.toFile().setExecutable(true);
        }

        return ytDlpPath;
    }
}
