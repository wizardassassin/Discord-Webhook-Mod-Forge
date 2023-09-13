package com.wizard_assassin.discord_webhook_mod;

import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerAdvancementEventHandler {
    @SubscribeEvent
    public static void playerAdvancement(AdvancementEvent event) {
        DiscordWebhookMod.logger.info("\033[0;32m Player got Advancement! \033[0m");
    }
}
