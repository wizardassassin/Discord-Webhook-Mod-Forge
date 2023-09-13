package com.wizard_assassin.discord_webhook_mod.connection_handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FilePart extends StreamPart {
    private File file;

    public FilePart(File file, String name, String filename) {
        this.contentType = "application/octet-stream";
        this.file = file;
        this.name = name;
        this.filename = filename;
    }

    public void pipeFormData(OutputStream outStream) throws IOException {
        FileInputStream inputStream = new FileInputStream(this.file);
        ConnectionHandler.pipeStreams(inputStream, outStream);
    }
}