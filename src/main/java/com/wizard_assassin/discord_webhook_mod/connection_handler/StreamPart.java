package com.wizard_assassin.discord_webhook_mod.connection_handler;

public abstract class StreamPart extends FormPart {
    protected String filename;

    @Override
    public String getContentDisposition() {
        return super.getContentDisposition() + String.format("; filename=\"%s\"", this.filename);
    }
}