package com.wizard_assassin.discord_webhook_mod.connection_handler;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class ImagePart extends StreamPart {
    private BufferedImage image;

    public ImagePart(BufferedImage image, String name, String filename) {
        this.contentType = "image/png";
        this.image = image;
        this.name = name;
        this.filename = filename;
    }

    public void pipeFormData(OutputStream outStream) throws IOException {
        ImageIO.write(this.image, "png", outStream);
    }
}