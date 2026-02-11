package org.yt.dlp.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class YtDlpGuiFx extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/yt-dlp/gui/FXMLGui.fxml")
        );
        Parent root = loader.load();
//        Parent root = FXMLLoader.load(getClass().getResource("/yt-dlp/gui/FXMLGui.fxml"));
//        URL url = getClass().getResource("/yt-dlp/gui/FXMLGui.fxml");
//        System.out.println(url);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
