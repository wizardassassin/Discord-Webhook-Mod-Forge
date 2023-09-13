package com.wizard_assassin.discord_webhook_mod.connection_handler;

import java.io.IOException;
import java.io.OutputStream;

public abstract class FormPart {
    protected String contentType;
    protected String name;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentDisposition() {
        return String.format("form-data; name=\"%s\"", this.name);
    }

    public void changeContentType(String contentType) {
        this.contentType = contentType;
    }

    public abstract void pipeFormData(OutputStream outStream) throws IOException;
}