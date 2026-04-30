package org.yt.dlp.gui;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());


        Scene scene = new Scene(root);

        stage.setTitle("Downvid 0.3");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
