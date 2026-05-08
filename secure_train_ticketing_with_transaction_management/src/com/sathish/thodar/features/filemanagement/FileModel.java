package com.sathish.thodar.features.filemanagement;

import java.lang.String;
import java.lang.Long;

public class FileModel {
    private String fileName;
    private String filePath;
    private Long fileSize;

    public FileModel() {}

    public FileModel(String fileName, String filePath, Long fileSize) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
}