package com.wizard_assassin.discord_webhook_mod;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.wizard_assassin.discord_webhook_mod.connection_handler.ConnectionHandler;

import net.minecraft.entity.player.EntityPlayer;

public class ImageHandler {
    public static long timeoutDelay = 600000;
    public static String sessionServer = "https://sessionserver.mojang.com/session/minecraft/profile/";
    public static String textureServer = "https://textures.minecraft.net/texture/";
    public static String resourceFolder = "/assets/" + DiscordWebhookMod.MODID + "/textures/";
    public static String[] legacySkins = {
            "steve_legacy.png",
            "alex_legacy.png"
    };

    public static String[] skins = {
            "alex.png",
            "ari.png",
            "efe.png",
            "kai.png",
            "makena.png",
            "noor.png",
            "steve.png",
            "sunny.png",
            "zuri.png"
    };

    public static String defaultServerIcon = "unknown_server_64.png";

    private HashMap<UUID, BufferedImage> imageMap;
    private HashMap<UUID, Long> ttlMap;

    public ImageHandler() {
        this.imageMap = new HashMap<UUID, BufferedImage>();
        this.ttlMap = new HashMap<UUID, Long>();
    }

    public BufferedImage getAvatar(EntityPlayer player, boolean isLeaving) {
        long now = System.currentTimeMillis();
        UUID uuid = player.getUniqueID();
        if (this.ttlMap.containsKey(uuid))
            if (isLeaving || now - this.ttlMap.get(uuid) < timeoutDelay)
                return this.imageMap.get(uuid);
        BufferedImage avatar;
        try {
            String textureString = ImageHandler.entityPlayerToTexture(player);
            JsonObject texture = new JsonParser().parse(textureString).getAsJsonObject();
            String url = texture.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
            String textureId = url.substring(url.lastIndexOf("/") + 1);
            String textureUrl = ImageHandler.textureServer + textureId;
            BufferedImage image = ConnectionHandler.getImage(textureUrl);
            if (image.getWidth() != 64 || (image.getHeight() != 64 && image.getHeight() != 32))
                throw new IOException("Invalid image size");
            if (image.getHeight() != 32)
                image = ImageHandler.convertImage(image);
            avatar = ImageHandler.convertSkinImage(image);
        } catch (JsonParseException | NoSuchElementException | IOException e) {
            DiscordWebhookMod.logger.error(uuid.toString());
            e.printStackTrace();
            BufferedImage defaultSkin = ImageHandler.getDefaultSkin(player.getUniqueID());
            avatar = ImageHandler.convertSkinImage(defaultSkin);
        }
        Long now2 = System.currentTimeMillis();
        this.imageMap.put(uuid, avatar);
        this.ttlMap.put(uuid, now2);
        return avatar;
    }

    public static boolean clearIfNoAlpha(BufferedImage image) {
        int[] imageData = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        for (int y = 0; y < 16; y++)
            for (int x = 32; x < 64; x++)
                if (imageData[y * 64 + x] >> 24 == 0)
                    return true;
        for (int y = 0; y < 16; y++)
            for (int x = 32; x < 64; x++)
                imageData[y * 64 + x] = 0;
        return false;
    }

    public static BufferedImage getDefaultSkin(UUID uuid) {
        String skin = ImageHandler.skins[Math.floorMod(uuid.hashCode(), ImageHandler.skins.length)];
        return ImageHandler.grabSkin(skin);
    }

    public static BufferedImage getDefaultSkinLegacy(UUID uuid) {
        String skin = ImageHandler.legacySkins[uuid.hashCode() & 1];
        return ImageHandler.grabSkin(skin);
    }

    private static BufferedImage grabSkin(String resourcePath) {
        try {
            return ImageHandler.resourceToImage(resourcePath);
        } catch (IOException | IllegalArgumentException e) {
            DiscordWebhookMod.logger.error(resourcePath);
            e.printStackTrace();
            BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
            return image;
        }
    }

    private static BufferedImage resourceToImage(String resourcePath) throws IOException, IllegalArgumentException {
        InputStream stream = DiscordWebhookMod.objClass.getResourceAsStream(ImageHandler.resourceFolder + resourcePath);
        return ImageIO.read(stream);
    }

    public static BufferedImage convertImage(BufferedImage image) {
        BufferedImage image2 = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = image2.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        ImageHandler.clearIfNoAlpha(image2);
        return image2;
    }

    public static String uuidToTexture(UUID uuid) throws IOException {
        String sessionUrl = ImageHandler.sessionServer + uuid.toString().replace("-", "");
        JsonObject res = ConnectionHandler.getJson(sessionUrl);
        String b64Str = res.getAsJsonArray("properties").get(0).getAsJsonObject().get("value").getAsString();
        byte[] decoded = Base64.getDecoder().decode(b64Str);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    public static String entityPlayerToTexture(EntityPlayer player) throws NoSuchElementException {
        String b64Str = player.getGameProfile().getProperties().get("textures").iterator().next().getValue();
        byte[] decoded = Base64.getDecoder().decode(b64Str);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    public static BufferedImage convertSkinImage(BufferedImage image) {
        BufferedImage avatar = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = avatar.createGraphics();
        graphics.drawImage(image, 0, 0, avatar.getWidth(), avatar.getHeight(), 8, 8, 16, 16, null);
        graphics.drawImage(image, 0, 0, avatar.getWidth(), avatar.getHeight(), 40, 8, 48, 16, null);
        graphics.dispose();
        return avatar;
    }

    public static BufferedImage getServerIcon(File icon) {
        try {
            if (!icon.exists())
                return ImageHandler.resourceToImage(ImageHandler.defaultServerIcon);
            BufferedImage serverIcon = ImageIO.read(icon);
            if (serverIcon.getWidth() != 64 || serverIcon.getHeight() != 64)
                throw new IOException("Invalid sever-icon size");
            return serverIcon;
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = image.createGraphics();
            graphics.setColor(new Color(0, 0, 0, 255));
            graphics.fillRect(0, 32, 32, 64);
            graphics.fillRect(32, 0, 64, 32);
            graphics.setColor(new Color(251, 62, 249, 255));
            graphics.fillRect(0, 0, 32, 32);
            graphics.fillRect(32, 32, 64, 64);
            graphics.dispose();
            return image;
        }
    }
}
