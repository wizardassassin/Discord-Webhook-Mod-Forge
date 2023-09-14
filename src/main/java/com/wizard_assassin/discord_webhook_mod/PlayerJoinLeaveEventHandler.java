package com.wizard_assassin.discord_webhook_mod;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class PlayerJoinLeaveEventHandler {
    @SubscribeEvent
    public static void playerLoggedIn(PlayerLoggedInEvent event) {
        DiscordWebhookMod.webhook.sendJoin(event.player);
        DiscordWebhookMod.logger.info("\033[0;32m Player logged in! \033[0m");
    }

    @SubscribeEvent
    public static void playerLoggedOut(PlayerLoggedOutEvent event) {
        DiscordWebhookMod.webhook.sendLeave(event.player);
        DiscordWebhookMod.logger.info("\033[0;32m Player logged out! \033[0m");
    }
}
