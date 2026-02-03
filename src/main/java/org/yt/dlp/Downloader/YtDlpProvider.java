package org.yt.dlp.Downloader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class YtDlpProvider {

    private static Path ytDlpPath;

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
        }
        else if (isLinuxOs) {
            resourceName = "yt-dlp/linux/yt-dlp";
            fileName = "yt-dlp";
            cachePath = Paths.get(
                    System.getProperty("user.home"),
                    ".cache",
                    "yt-dlp-downloader"
            );
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
}
