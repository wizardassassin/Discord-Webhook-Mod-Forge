package com.wizard_assassin.discord_webhook_mod;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.JsonArray;

import com.google.gson.JsonObject;

public class DiscordWebhookSender {
    public static String username = "Test";
    public static String avatarUrl = "";

    private String url;
    private JsonObject jsonBody;
    public JsonArray attachments;
    public JsonObject embed;
    public JsonObject author;

    public DiscordWebhookSender(String url) {
        this.url = url;
        this.jsonBody = new JsonObject();
        JsonArray embeds = new JsonArray();
        this.embed = new JsonObject();
        embeds.add(this.embed);
        this.jsonBody.add("embeds", embeds);
        this.author = new JsonObject();
        this.author.addProperty("icon_url", "attachment://avatar.png");
        this.attachments = new JsonArray();
        JsonObject attachment = new JsonObject();
        attachment.addProperty("id", 0);
        attachment.addProperty("filename", "avatar.png");
        this.attachments.add(attachment);
    }

    public void sendJoin() {
    }

    public void sendLeave() {
    }

    public void sendStart() {
    }

    public void sendStop() {
    }
}
