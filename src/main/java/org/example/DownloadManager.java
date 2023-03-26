package org.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.config.AppConfig;
import org.example.models.FileInfo;

import java.io.File;
import java.text.DecimalFormat;

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
        FileInfo file = new FileInfo((index+1)+"", fileName, url, status, action, path, "0");
        this.index++;
        DownloadThread downloadThread = new DownloadThread(file, this);
        this.tableView.getItems().add(Integer.parseInt(file.getIndex())-1,file);
        downloadThread.start();
        this.fileUrl.setText("");
    }

    public void updateUI(FileInfo file) {
        FileInfo fileInfo = this.tableView.getItems().get(Integer.parseInt(file.getIndex()) - 1);
        fileInfo.setStatus(file.getStatus());
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        fileInfo.setPercent(decimalFormat.format(Double.parseDouble(file.getPercent())));
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

        TableColumn<FileInfo, String> percent = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(4);
        percent.setCellValueFactory(p -> {
            SimpleStringProperty simpleStringProperty = new SimpleStringProperty();
            simpleStringProperty.set(p.getValue().getPercent()+" %");
            return simpleStringProperty;
        });

        TableColumn<FileInfo, String> action = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(5);
        action.setCellValueFactory(p -> {
            return p.getValue().actionProperty();
        });
    }
}
