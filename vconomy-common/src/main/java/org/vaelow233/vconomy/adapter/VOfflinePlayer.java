package org.vaelow233.vconomy.adapter;

import java.util.UUID;

public abstract class VOfflinePlayer {
    protected UUID uuid;
    protected String name;

    protected VOfflinePlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Create a VOfflinePlayer instance without an uuid (used for console or api operator)
     */
    public static VOfflinePlayer ofNonPlayer(String name) {
        return new VOfflinePlayer(null, name) {};
    }
}
