package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.config.AppConfig;
import org.example.models.FileInfo;

import java.io.File;

public class DownloadManager {
    @FXML
    private TextField fileUrl;
    @FXML
    private TableView<FileInfo> tableView;
    private int index = 0;

    @FXML
    void onDownloadClick(ActionEvent event) {
        String url = this.fileUrl.getText().trim();
        String fileName = url.substring(url.lastIndexOf("/")+1);
        String status = "Starting";
        String action = "Open";
        String path = AppConfig.DOWNLOAD_PATH + File.separator + fileName;
        FileInfo file = new FileInfo((index+1)+"", fileName, url, status, action, path);
        this.index++;
        DownloadThread downloadThread = new DownloadThread(file, this);
        this.tableView.getItems().add(Integer.parseInt(file.getIndex())-1,file);
        downloadThread.start();
        this.fileUrl.setText("");
    }

    public void updateUI(FileInfo file) {
        FileInfo fileInfo = this.tableView.getItems().get(Integer.parseInt(file.getIndex()) - 1);
        fileInfo.setStatus(file.getStatus());
        this.tableView.refresh();
    }

    @FXML
    public void initialize(){
        TableColumn<FileInfo, String> sn = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(0);
        sn.setCellValueFactory(p -> {
            return p.getValue().indexProperty();
        });

        TableColumn<FileInfo, String> name = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(1);
        name.setCellValueFactory(p -> {
            return p.getValue().nameProperty();
        });

        TableColumn<FileInfo, String> url = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(2);
        url.setCellValueFactory(p -> {
            return p.getValue().urlProperty();
        });

        TableColumn<FileInfo, String> status = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(3);
        status.setCellValueFactory(p -> {
            return p.getValue().statusProperty();
        });

        TableColumn<FileInfo, String> action = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(4);
        action.setCellValueFactory(p -> {
            return p.getValue().actionProperty();
        });
    }
}
