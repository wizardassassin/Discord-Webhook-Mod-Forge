package com.wizard_assassin.discord_webhook_mod;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = DiscordWebhookMod.MODID)
public class CustomConfig {
    @Comment({
            "A Valid Discord Webhook URL",
            "Server needs to be restarted for changes to take effect"
    })
    public static String url = "INSERT_VALID_URL_IN_CONFIG_FILE";
}
