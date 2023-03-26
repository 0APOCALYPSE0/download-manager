package org.example;

import org.example.models.FileInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
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
            /**
             * with Files.copy we can not calculate downloading percentage
             */
//            Files.copy(new URL(this.file.getUrl()).openStream(), Paths.get(this.file.getPath()));
            URL url = new URL(this.file.getUrl());
            URLConnection urlConnection = url.openConnection();
            int fileSize = urlConnection.getContentLength();
            int countByte = 0;
            double byteSum = 0.0;
            double percent = 0.0;
            BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
            FileOutputStream fos = new FileOutputStream(this.file.getPath());
            byte data[] = new byte[1024];
            while(true){
                countByte = bufferedInputStream.read(data, 0, 1024);
                if(countByte == -1){
                    break;
                }
                fos.write(data, 0, countByte);
                byteSum = byteSum + countByte;
                if(fileSize > 0){
                    percent = (byteSum / fileSize) * 100;
                    this.file.setPercent(percent+"");
                    this.downloadManager.updateUI(this.file);
                }
            }
            bufferedInputStream.close();
            fos.close();
            this.file.setStatus("Done");
        } catch (IOException e) {
            this.file.setStatus("Failed");
            System.out.println("Downloading Error");
            e.printStackTrace();
        }
        this.downloadManager.updateUI(this.file);
    }
}
