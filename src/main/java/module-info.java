module org.yt.dlp.gui {

    requires javafx.controls;
    requires javafx.fxml;
    requires atlantafx.base;

    requires java.desktop;
    requires java.logging;
    requires java.prefs;

    opens org.yt.dlp.gui to javafx.fxml;
    exports org.yt.dlp.gui;
}
