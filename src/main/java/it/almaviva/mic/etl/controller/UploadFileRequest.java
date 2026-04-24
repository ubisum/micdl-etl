package it.almaviva.mic.etl.controller;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;

public class UploadFileRequest {

    @Schema(type = "string", format = "binary", description = "File da caricare")
    private MultipartFile file;

    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }
}