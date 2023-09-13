package com.wizard_assassin.discord_webhook_mod.connection_handler;

import java.io.IOException;
import java.io.OutputStream;

import com.google.gson.JsonObject;

public class JsonPart extends FormPart {
    private JsonObject jsonBody;

    public JsonPart(JsonObject jsonBody, String name) {
        this.contentType = "application/json";
        this.jsonBody = jsonBody;
        this.name = name;
    }

    public void pipeFormData(OutputStream outStream) throws IOException {
        outStream.write(jsonBody.toString().getBytes());
    }
}