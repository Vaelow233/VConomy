package org.vaelow233.vconomy.bukkit.adapter;

import org.bukkit.OfflinePlayer;
import org.vaelow233.vconomy.adapter.VOfflinePlayer;

public class SpigotOfflinePlayer extends VOfflinePlayer {
    private final OfflinePlayer offlinePlayer;

    public SpigotOfflinePlayer(OfflinePlayer offlinePlayer) {
        super(offlinePlayer.getUniqueId(), offlinePlayer.getName());
        this.offlinePlayer = offlinePlayer;
    }
}
