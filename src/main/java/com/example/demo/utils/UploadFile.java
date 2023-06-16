package com.example.demo.utils;

import com.example.demo.entity.board.BoardFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;
    private Long id;

    public UploadFile(String uploadFileName, String storeFileName, Long id) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.id = id;
    }

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

    public UploadFile(BoardFile o) {
        this.uploadFileName = o.getUploadFileName();
        this.storeFileName = o.getStoreFileName();
        this.id = o.getId();
    }
}
