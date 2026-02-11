package org.yt.dlp.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.yt.dlp.Downloader.YtDlpDownloader;
import org.yt.dlp.Downloader.YtDlpDownloaderTask;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FXMLGuiController implements Initializable {

    @FXML
    ProgressBar downloadProgressBar;

    @FXML
    public Button downloadButton;

    @FXML
    private Label status;

    @FXML
    private ChoiceBox<String> videoFileType;

    @FXML
    private TextField videoUrlTextfield;

    @FXML
    public void buttonActionHandler(ActionEvent event) throws IOException {

        String downloadUrl = videoUrlTextfield.getText();
        String downloadType = videoFileType.getValue();

        videoUrlTextfield.setText("");

        YtDlpDownloaderTask downloadTask = new YtDlpDownloaderTask(downloadUrl, downloadType);

        downloadProgressBar.progressProperty().bind(downloadTask.progressProperty());
        status.textProperty().bind(downloadTask.messageProperty());

        downloadTask.setOnSucceeded(e -> {
            downloadProgressBar.progressProperty().unbind();
            downloadProgressBar.setProgress(1.0);
            status.textProperty().unbind();
            status.setText("Done!");
        });

        downloadTask.setOnFailed(e -> {
            downloadProgressBar.progressProperty().unbind();
            downloadProgressBar.setProgress(0);
            status.textProperty().unbind();
            status.setText("Error while downloading");
        });

        new Thread(downloadTask).start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> videoTypes = FXCollections.observableArrayList(
                 "mp4",
                    "mov",
                    "mp3"
        );

        videoFileType.setItems(videoTypes);
        videoFileType.setValue("mp4");
    }

}
