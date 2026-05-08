package com.sathish.thodar.features.filemanagement;

import java.lang.String;
import java.lang.System;

public class FileView {
    
    public void printExportSuccess(String fileName, String filePath) {
        System.out.println("\n[SUCCESS] File exported successfully!");
        System.out.println("File Name: " + fileName);
        System.out.println("Saved at: " + filePath);
    }
    
    public void printExportFailure(String errorMessage) {
        System.out.println("\n[ERROR] Failed to export file: " + errorMessage);
    }
}