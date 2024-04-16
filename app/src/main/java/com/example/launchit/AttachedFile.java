package com.example.launchit;

import android.net.Uri;

public class AttachedFile {
    private String fileName;
    private Uri fileUri;

    public AttachedFile(String fileName, Uri fileUri) {
        this.fileName = fileName;
        this.fileUri = fileUri;
    }

    public String getFileName() {
        return fileName;
    }

    public Uri getFileUri() {
        return fileUri;
    }
}
