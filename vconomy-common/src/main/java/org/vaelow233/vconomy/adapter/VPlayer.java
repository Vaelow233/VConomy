package org.vaelow233.vconomy.adapter;

import java.util.UUID;

public abstract class VPlayer extends VCommandSender {
    protected VPlayer(UUID uuid, String name) {
        super(uuid, name);
    }

    public abstract void kick(String reason);
}
