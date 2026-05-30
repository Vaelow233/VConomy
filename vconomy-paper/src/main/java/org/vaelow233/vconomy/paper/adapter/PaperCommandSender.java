package org.vaelow233.vconomy.paper.adapter;

import org.bukkit.command.CommandSender;
import org.vaelow233.vconomy.adapter.VCommandSender;

public class PaperCommandSender extends VCommandSender {
    private final CommandSender sender;

    public PaperCommandSender(CommandSender sender) {
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
