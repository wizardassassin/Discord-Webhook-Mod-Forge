package com.wizard_assassin.discord_webhook_mod;

import java.io.File;
import java.nio.file.Paths;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class ConfigReloader extends CommandBase {
    @Override
    public String getName() {
        return "updateDiscordWebhook";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/updateDiscordWebhook";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        DiscordWebhookMod.config.loadConfig();
        DiscordWebhookMod.webhook.updateURL(DiscordWebhookMod.config.url);
        File serverIcon = Paths.get(server.getDataDirectory().toPath().toString(), "server-icon.png").toFile();
        String version = server.getMinecraftVersion();
        String motd = server.getMOTD();
        String maxPlayers = "" + server.getMaxPlayers();
        String players = "" + server.getCurrentPlayerCount() + "/" + maxPlayers;
        DiscordWebhookMod.webhook.sendStarted(serverIcon, version, motd, players, DiscordWebhookMod.timeStore);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }
}
