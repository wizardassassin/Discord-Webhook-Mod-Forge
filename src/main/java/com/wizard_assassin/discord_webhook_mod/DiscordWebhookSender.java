package com.wizard_assassin.discord_webhook_mod;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;

import com.google.gson.JsonObject;
import com.wizard_assassin.discord_webhook_mod.connection_handler.ConnectionHandler;
import com.wizard_assassin.discord_webhook_mod.connection_handler.FormPart;
import com.wizard_assassin.discord_webhook_mod.connection_handler.ImagePart;
import com.wizard_assassin.discord_webhook_mod.connection_handler.JsonPart;

import net.minecraft.entity.player.EntityPlayer;

public class DiscordWebhookSender {
    public static String username = "Test";
    public static String avatarUrl = "";

    private String url;
    private JsonObject jsonBody;
    public JsonArray attachments;
    public JsonObject embed;
    public JsonObject author;
    public JsonArray fields;
    public JsonObject version;
    public JsonObject motd;
    public JsonObject players;
    public JsonObject thumbnail;

    public DiscordWebhookSender(String url) {
        this.url = url;
        this.jsonBody = new JsonObject();
        JsonArray embeds = new JsonArray();
        this.embed = new JsonObject();
        embeds.add(this.embed);
        this.jsonBody.add("embeds", embeds);
        this.author = new JsonObject();
        this.author.addProperty("icon_url", "attachment://image.png");
        this.attachments = new JsonArray();
        JsonObject attachment = new JsonObject();
        attachment.addProperty("id", 0);
        attachment.addProperty("filename", "image.png");
        this.attachments.add(attachment);
        this.fields = new JsonArray();
        this.version = new JsonObject();
        this.version.addProperty("name", "Version");
        this.motd = new JsonObject();
        this.motd.addProperty("name", "MOTD");
        this.players = new JsonObject();
        this.players.addProperty("name", "Players");
        this.fields.add(this.version);
        this.fields.add(this.motd);
        this.fields.add(this.players);
        this.thumbnail = new JsonObject();
        this.thumbnail.addProperty("url", "attachment://image.png");
    }

    private void setStart(String title, String version, String motd, String players) {
        this.embed.remove("title");
        this.embed.remove("author");
        this.version.addProperty("value", version);
        this.motd.addProperty("value", motd);
        this.players.addProperty("value", players);
        this.jsonBody.add("attachments", this.attachments);
        this.embed.add("fields", this.fields);
        this.embed.add("thumbnail", this.thumbnail);
        this.embed.addProperty("title", title);
    }

    private void setStop(String title) {
        this.jsonBody.remove("attachments");
        this.embed.remove("author");
        this.embed.remove("fields");
        this.embed.remove("thumbnail");
        this.embed.addProperty("title", title);
    }

    private void setPlayer(String name) {
        this.embed.remove("title");
        this.embed.remove("fields");
        this.embed.remove("thumbnail");
        this.jsonBody.add("attachments", this.attachments);
        this.embed.add("author", this.author);
        this.author.addProperty("name", name);
    }

    private void setTimestamp() {
        this.embed.addProperty("timestamp", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));
    }

    private void setColor(int color) {
        this.embed.addProperty("color", color);
    }

    private List<FormPart> createParts(BufferedImage image) {
        return Arrays.asList(new JsonPart(this.jsonBody, "payload_json"),
                new ImagePart(image, "files[0]", "image.png"));
    }

    public void sendJoin(EntityPlayer player, String playerCount) {
        BufferedImage avatar = DiscordWebhookMod.avatars.getAvatar(player, false);
        List<FormPart> parts = this.createParts(avatar);
        this.setPlayer(player.getName() + " joined the server (" + playerCount + ")");
        this.setColor(2067276);
        this.setTimestamp();
        try {
            ConnectionHandler.sendMultipart(this.url, parts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLeave(EntityPlayer player, String playerCount) {
        BufferedImage avatar = DiscordWebhookMod.avatars.getAvatar(player, true);
        List<FormPart> parts = this.createParts(avatar);
        this.setPlayer(player.getName() + " left the server (" + playerCount + ")");
        this.setColor(2067276);
        this.setTimestamp();
        try {
            ConnectionHandler.sendMultipart(this.url, parts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendStarted(File icon, String version, String motd, String players) {
        BufferedImage serverIcon = ImageHandler.getServerIcon(icon);
        List<FormPart> parts = this.createParts(serverIcon);
        this.setStart("Minecraft Server has Started", version, motd, players);
        this.setColor(2123412);
        this.setTimestamp();
        try {
            ConnectionHandler.sendMultipart(this.url, parts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendStopped() {
        this.setStop("Minecraft Server has Stopped");
        this.setColor(2123412);
        this.setTimestamp();
        try {
            ConnectionHandler.sendJson(this.url, this.jsonBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
