package org.yt.dlp.util;

import java.io.IOException;

// Checking for system instances of dependencies
public class DependCheck {
    public static boolean ffmpegCheck() {
        try {
            Process process = new ProcessBuilder("ffmpeg", "-version").start();
            int exitCode = process.waitFor();

            return exitCode == 0;

        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    public static boolean ytDlpCheck() {
        try {
            Process process = new ProcessBuilder("yt-dlp", "--version").start();
            int exitCode = process.waitFor();

            return exitCode == 0;

        } catch (IOException | InterruptedException e) {

            return false;

        }
    }
}
