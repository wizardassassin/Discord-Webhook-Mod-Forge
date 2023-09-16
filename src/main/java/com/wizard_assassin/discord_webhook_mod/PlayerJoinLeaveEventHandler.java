package com.wizard_assassin.discord_webhook_mod;

import java.util.HashSet;
import java.util.UUID;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class PlayerJoinLeaveEventHandler {
    private static HashSet<UUID> loggedInPlayers = new HashSet<UUID>();

    @SubscribeEvent
    public static void playerLoggedIn(PlayerLoggedInEvent event) {
        loggedInPlayers.add(event.player.getUniqueID());
        String playerCount = "" + loggedInPlayers.size() + "/" + DiscordWebhookMod.maxPlayers;
        DiscordWebhookMod.webhook.sendJoin(event.player, playerCount);
        DiscordWebhookMod.logger.info("\033[0;32m Player logged in! \033[0m");
    }

    @SubscribeEvent
    public static void playerLoggedOut(PlayerLoggedOutEvent event) {
        loggedInPlayers.remove(event.player.getUniqueID());
        String playerCount = "" + loggedInPlayers.size() + "/" + DiscordWebhookMod.maxPlayers;
        DiscordWebhookMod.webhook.sendLeave(event.player, playerCount);
        DiscordWebhookMod.logger.info("\033[0;32m Player logged out! \033[0m");
    }
}
