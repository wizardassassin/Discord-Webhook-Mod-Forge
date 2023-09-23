package com.wizard_assassin.discord_webhook_mod;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class CustomConfig {
    public String url;
    private Configuration config;

    public CustomConfig(File configFile) {
        this.config = new Configuration(configFile);
        this.url = null;
        this.loadConfig();
    }

    public void loadConfig() {
        boolean result = this.internalLoadConfig();
        if (!result)
            DiscordWebhookMod.logger.warn("The config file URL is still the same.");

    }

    public boolean internalLoadConfig() {
        this.config.load();
        String configUrl = this.config.getString("url", Configuration.CATEGORY_GENERAL,
                "INSERT_URL_IN_CONFIG_FILE",
                "A Valid Discord Webhook URL\nrun /discordWebhookUpdate (or restart the server)\nfor changes to take effect");
        if (this.url != null && this.url.equals(configUrl)) {
            this.config.save();
            return false;
        }
        this.url = configUrl;
        this.config.save();
        return true;
    }
}
