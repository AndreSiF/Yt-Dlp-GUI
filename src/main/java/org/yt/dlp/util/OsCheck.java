package org.yt.dlp.util;

import java.nio.file.Paths;

public class OsCheck {

    private static final String os = System.getProperty("os.name").toLowerCase();
    private static final boolean isWindowsOs = os.contains("win");
    private static final boolean isLinuxOs = os.contains("linux");
    private static final boolean isMacOs = os.contains("mac");

    public enum OS{
        WIN, LIN, MAC
    }

    public static OS getSystemOs(){

        String os = System.getProperty("os.name").toLowerCase();
        final boolean ISWINDOWS = os.contains("win");
        final boolean ISLINUX = os.contains("linux");
        final boolean ISMAC = os.contains("mac");
        OS systemOS;

        if(ISWINDOWS){
            systemOS = OS.WIN;
        }
        else if (ISLINUX) {
            systemOS = OS.LIN;
        }
        else if (ISMAC) {
            systemOS = OS.MAC;
        }
        else {
            throw new RuntimeException("Operating system not supported");
        }

        return systemOS;
    }
}
