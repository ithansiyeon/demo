package com.example.demo.utils;

import com.example.demo.entity.BoardFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;
    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

    public UploadFile(BoardFile o) {
        this.uploadFileName = o.getUploadFileName();
        this.storeFileName = o.getStoreFileName();
    }
}
