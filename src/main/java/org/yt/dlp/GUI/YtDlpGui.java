package org.yt.dlp.GUI;

import org.yt.dlp.Downloader.YtDlpDownloader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class YtDlpGui implements ActionListener {

    private JTextField videoUrlTextField;
    private JLabel videoUrlLabel;
    private JLabel statusLabel;
    private JFrame frame;
    private JPanel panel;
    private JButton baixarButton;

    public YtDlpGui() {

        frame = new JFrame();
        panel = new JPanel();

        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        //panel.setLayout(new FlowLayout());

        videoUrlLabel = new JLabel("Link do video");
        videoUrlTextField = new JTextField(100);
        videoUrlTextField.addActionListener(this);

        baixarButton = new JButton("Baixar");
        baixarButton.addActionListener(this);

        statusLabel = new JLabel("");
        statusLabel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(videoUrlLabel);
        panel.add(videoUrlTextField);
        panel.add(baixarButton);
        panel.add(statusLabel);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Yt-Dlp Downloader");
        frame.pack();
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        new YtDlpGui();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                String videoUrl = videoUrlTextField.getText();
                videoUrlTextField.setText("");
                String status = new YtDlpDownloader().DownloadMp4(videoUrl);
                statusLabel.setText(status);
                return null;
            }
        }.execute();
    }
}
