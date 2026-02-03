package org.yt.dlp.GUI;

import org.yt.dlp.Downloader.YtDlpDownloader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class YtDlpGui implements ActionListener {

    final private JTextField videoUrlTextField;
    final private JLabel videoUrlLabel;
    //final private JLabel statusLabel;
    final private JFrame frame;
    final private JPanel panel;
    final private JButton downloadButton;
    final private String[] downloadChoice = {"mp4", "mov", "mp3"};
    final private JComboBox<String> downloadType = new JComboBox<String>(downloadChoice);

    public YtDlpGui() {

        frame = new JFrame();
        panel = new JPanel();

        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        videoUrlLabel = new JLabel("Link do video");
        videoUrlTextField = new JTextField(100);
        videoUrlTextField.addActionListener(this);
        videoUrlLabel.setVisible(true);
        videoUrlTextField.setVisible(true);

        downloadButton = new JButton("Baixar");
        downloadButton.addActionListener(this);
        downloadButton.setVisible(true);

        //statusLabel = new JLabel("");
//        statusLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
//        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        statusLabel.setVisible(true);

        downloadType.setVisible(true);

        panel.add(videoUrlLabel);
        panel.add(videoUrlTextField);
        panel.add(downloadButton);
        //panel.add(statusLabel);
        panel.add(downloadType);

        frame.add(panel);
        frame.setSize(1124, 576);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Yt-Dlp Downloader");
        frame.setLocationRelativeTo(null);
        //frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new YtDlpGui();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String videoUrl = videoUrlTextField.getText();
        String downloadExtension = downloadType.getItemAt(downloadType.getSelectedIndex());
        videoUrlTextField.setText("");
        System.out.println(downloadExtension);
        String status = YtDlpDownloader.DownloadVideo(videoUrl, downloadExtension);
        //statusLabel.setText(status);
    }
}
