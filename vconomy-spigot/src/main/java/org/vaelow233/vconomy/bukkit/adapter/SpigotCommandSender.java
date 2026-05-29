package org.vaelow233.vconomy.bukkit.adapter;

import org.bukkit.command.CommandSender;
import org.vaelow233.vconomy.adapter.VCommandSender;

public class SpigotCommandSender extends VCommandSender {
    private final CommandSender sender;

    public SpigotCommandSender(CommandSender sender) {
        super(null, sender.getName());
        this.sender = sender;
    }

    @Override
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return sender.hasPermission(permission);
    }
}
