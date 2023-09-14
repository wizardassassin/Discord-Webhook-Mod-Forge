package com.wizard_assassin.discord_webhook_mod;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.server.FMLServerHandler;

import java.io.File;
import java.nio.file.Paths;

import org.apache.logging.log4j.Logger;

@Mod(modid = DiscordWebhookMod.MODID, name = DiscordWebhookMod.NAME, version = DiscordWebhookMod.VERSION, serverSideOnly = true, acceptableRemoteVersions = "*")
public class DiscordWebhookMod {
    public static final String MODID = "discord-webhook-mod";
    public static final String NAME = "Discord Webhook Mod";
    public static final String VERSION = "1.0";

    public static Logger logger;
    public static DiscordWebhookSender webhook;
    public static ImageHandler avatars;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        webhook = new DiscordWebhookSender("ADD_URL_HERE");
        avatars = new ImageHandler();
        // webhook.sendStarting();
        logger.info("\033[0;32m STARTING \033[0m");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(PlayerJoinLeaveEventHandler.class);
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        MinecraftServer server = FMLServerHandler.instance().getServer();
        // assume server.getCurrentPlayerCount(); == 0
        File serverIcon = Paths.get(server.getDataDirectory().toPath().toString(), "server-icon.png").toFile();
        String version = server.getMinecraftVersion();
        String motd = server.getMOTD();
        String players = "0/" + server.getMaxPlayers();
        webhook.sendStarted(serverIcon, version, motd, players);
        logger.info("\033[0;32m STARTED \033[0m");
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        // webhook.sendStopping();
        logger.info("\033[0;32m STOPPING \033[0m");
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        webhook.sendStopped();
        logger.info("\033[0;32m STOPPED \033[0m");
    }
}
