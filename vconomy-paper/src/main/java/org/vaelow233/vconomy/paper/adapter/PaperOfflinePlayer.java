package org.vaelow233.vconomy.paper.adapter;

import org.bukkit.OfflinePlayer;
import org.vaelow233.vconomy.adapter.VOfflinePlayer;

public class PaperOfflinePlayer extends VOfflinePlayer {
    private final OfflinePlayer offlinePlayer;

    public PaperOfflinePlayer(OfflinePlayer offlinePlayer) {
        super(offlinePlayer.getUniqueId(), offlinePlayer.getName());
        this.offlinePlayer = offlinePlayer;
    }
}
