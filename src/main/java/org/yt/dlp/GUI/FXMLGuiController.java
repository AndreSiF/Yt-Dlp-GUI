package org.yt.dlp.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.yt.dlp.Downloader.YtDlpDownloader;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLGuiController implements Initializable {

    @FXML
    public Button downloadButton;

    @FXML
    private Label status;

    @FXML
    private ChoiceBox<String> videoFileType;

    @FXML
    private TextField videoUrlTextfield;

    @FXML
    public void buttonActionHandler(ActionEvent event) {

        String downloadUrl = videoUrlTextfield.getText();
        String downloadType = videoFileType.getValue();

        videoUrlTextfield.setText("");
        String status = YtDlpDownloader.DownloadVideo(downloadUrl, downloadType);
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
