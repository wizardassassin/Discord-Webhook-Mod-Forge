package com.wizard_assassin.discord_webhook_mod;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class DiscordWebhookStatus extends CommandBase {
    @Override
    public String getName() {
        return "discordWebhookStatus";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/discordWebhookStatus";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String msg = (DiscordWebhookMod.webhook.canSend()) ? "The webhook is working!" : "The webhook isn't working...";
        CommandBase.notifyCommandListener(sender, this, msg);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (sender instanceof EntityPlayer) {
            return true;
        }
        return super.checkPermission(server, sender);
    }
}
