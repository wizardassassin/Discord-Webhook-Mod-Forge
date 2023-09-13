package com.wizard_assassin.discord_webhook_mod.connection_handler;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import com.google.gson.JsonElement;

// import javax.net.ssl.HttpsURLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConnectionHandler {
    public static String userAgent = "Java-Test";

    public static String sendJson(String urlString, JsonObject jsonBody, boolean readResponse) throws IOException {
        URL url = new URL(urlString);
        // HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("User-Agent", ConnectionHandler.userAgent);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();
        OutputStream outStream = connection.getOutputStream();
        outStream.write(jsonBody.toString().getBytes());
        outStream.flush();
        outStream.close();
        InputStream inStream = connection.getInputStream();
        String response = null;
        if (readResponse)
            response = ConnectionHandler.readInputStream2(inStream);
        inStream.close();
        connection.disconnect();
        return response;
    }

    public static String sendJson(String urlString, JsonObject jsonBody) throws IOException {
        return ConnectionHandler.sendJson(urlString, jsonBody, false);
    }

    public static String sendMultipart(String urlString, ArrayList<FormPart> formParts, boolean readResponse)
            throws IOException {
        String boundary = String.format("---data-%08d", (long) (Math.random() * 1e8));
        String crlf = "\r\n";
        String sep = "--";
        byte[] seperatorBoundary = (sep + boundary + crlf).getBytes();
        byte[] endBoundary = (sep + boundary + sep).getBytes();
        byte[] newLine = crlf.getBytes();
        URL url = new URL(urlString);
        // HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("User-Agent", ConnectionHandler.userAgent);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();
        OutputStream outStream = connection.getOutputStream();
        for (FormPart formPart : formParts) {
            String contentDisposition = formPart.getContentDisposition();
            String contentType = formPart.getContentType();
            outStream.write(seperatorBoundary);
            outStream.write(("Content-Disposition: " + contentDisposition + crlf).getBytes());
            if (!contentType.equals("text/plain"))
                outStream.write(("Content-Type: " + contentType + crlf).getBytes());
            outStream.write(newLine);
            formPart.pipeFormData(outStream);
            outStream.write(newLine);
        }
        outStream.write(endBoundary);
        outStream.flush();
        outStream.close();
        InputStream inStream = connection.getInputStream();
        String response = null;
        if (readResponse)
            response = ConnectionHandler.readInputStream2(inStream);
        inStream.close();
        connection.disconnect();
        return response;
    }

    public static String sendMultipart(String urlString, ArrayList<FormPart> formParts) throws IOException {
        return ConnectionHandler.sendMultipart(urlString, formParts, false);
    }

    public static String sendText(String urlString, String text, boolean readResponse) throws IOException {
        return null;
    }

    public static String sendText(String urlString, String text) throws IOException {
        return ConnectionHandler.sendText(urlString, text, false);
    }

    public static String getText(String urlString) throws IOException {
        URL url = new URL(urlString);
        // HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Accept", "text/plain, application/json");
        connection.addRequestProperty("User-Agent", ConnectionHandler.userAgent);
        connection.connect();
        InputStream inStream = connection.getInputStream();
        String response = ConnectionHandler.readInputStream2(inStream);
        inStream.close();
        connection.disconnect();
        return response;
    }

    public static JsonObject getJson(String urlString) throws IOException {
        URL url = new URL(urlString);
        // HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Accept", "application/json");
        connection.addRequestProperty("User-Agent", ConnectionHandler.userAgent);
        connection.connect();
        InputStream inStream = connection.getInputStream();
        JsonObject response = ConnectionHandler.readJson(inStream);
        inStream.close();
        connection.disconnect();
        return response;
    }

    public static BufferedImage getImage(String urlString) throws IOException {
        URL url = new URL(urlString);
        // HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("Accept", "image/png");
        connection.addRequestProperty("User-Agent", ConnectionHandler.userAgent);
        connection.connect();
        InputStream inStream = connection.getInputStream();
        BufferedImage image = ImageIO.read(inStream);
        inStream.close();
        connection.disconnect();
        return image;
    }

    public static String readInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = inStream.read(buffer)) != -1)
            outStream.write(buffer, 0, read);
        return new String(outStream.toByteArray(), StandardCharsets.UTF_8);
    }

    public static String readInputStream2(InputStream inStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        return reader.lines().collect(Collectors.joining());
    }

    public static JsonObject readJson(InputStream inStream) throws IOException {
        JsonElement element = new JsonParser().parse(new InputStreamReader(inStream));
        return element.getAsJsonObject();
    }

    public static void pipeStreams(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4096];
        int read;
        while ((read = inputStream.read(buffer)) != -1)
            outputStream.write(buffer, 0, read);
    }
}
