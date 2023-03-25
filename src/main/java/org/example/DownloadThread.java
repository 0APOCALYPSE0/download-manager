package org.example;

import org.example.models.FileInfo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadThread extends Thread{
    private FileInfo file;
    private DownloadManager downloadManager;

    public DownloadThread(FileInfo file, DownloadManager downloadManager){
        this.file = file;
        this.downloadManager = downloadManager;
    }

    @Override
    public void run() {
        //download logic
        this.file.setStatus("Downloading");
        this.downloadManager.updateUI(this.file);
        try {
            Files.copy(new URL(this.file.getUrl()).openStream(), Paths.get(this.file.getPath()));
            this.file.setStatus("Done");
        } catch (IOException e) {
            this.file.setStatus("Failed");
            System.out.println("Downloading Error");
            e.printStackTrace();
        }
        this.downloadManager.updateUI(this.file);
    }
}
