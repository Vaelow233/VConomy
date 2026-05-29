package org.vaelow233.vconomy.adapter;

import java.util.UUID;

public abstract class VCommandSender extends VOfflinePlayer {
    protected VCommandSender(UUID uuid, String name) {
        super(uuid, name);
    }

    public abstract void sendMessage(String message);
    public abstract boolean hasPermission(String permission);
}
