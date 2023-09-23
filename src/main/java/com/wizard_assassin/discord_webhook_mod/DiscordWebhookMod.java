package com.wizard_assassin.discord_webhook_mod;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
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
    public static CustomConfig config;
    public static Class<? extends DiscordWebhookMod> objClass;
    public static String maxPlayers;
    public static boolean hasStarted;
    public static long timeStore;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        timeStore = System.currentTimeMillis();
        logger = event.getModLog();
        config = new CustomConfig(event.getSuggestedConfigurationFile());
        webhook = new DiscordWebhookSender(config.url);
        avatars = new ImageHandler();
        objClass = this.getClass();
        // webhook.sendStarting();
        logger.info("\033[0;32m STARTING \033[0m");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(PlayerJoinLeaveEventHandler.class);
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ConfigReloader());
        event.registerServerCommand(new DiscordWebhookStatus());
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        timeStore = System.currentTimeMillis() - timeStore;
        if (!webhook.canSend()) {
            DiscordWebhookMod.config.internalLoadConfig();
            DiscordWebhookMod.webhook.updateURL(DiscordWebhookMod.config.url);
        }
        MinecraftServer server = FMLServerHandler.instance().getServer();
        File serverIcon = Paths.get(server.getDataDirectory().toPath().toString(), "server-icon.png").toFile();
        String version = server.getMinecraftVersion();
        String motd = server.getMOTD();
        maxPlayers = "" + server.getMaxPlayers();
        String players = "" + server.getCurrentPlayerCount() + "/" + maxPlayers;
        webhook.sendStarted(serverIcon, version, motd, players, timeStore);
        logger.info("\033[0;32m STARTED \033[0m");
    }

    @EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        timeStore = System.currentTimeMillis();
        // webhook.sendStopping();
        logger.info("\033[0;32m STOPPING \033[0m");
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        timeStore = System.currentTimeMillis() - timeStore;
        webhook.sendStopped(timeStore);
        logger.info("\033[0;32m STOPPED \033[0m");
    }
}
