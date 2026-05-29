package org.vaelow233.vconomy.bukkit.adapter;

import org.bukkit.entity.Player;
import org.vaelow233.vconomy.adapter.VPlayer;

public class SpigotPlayer extends VPlayer {

    private final Player player;

    public SpigotPlayer(Player player) {
        super(player.getUniqueId(), player.getName());
        this.player = player;
    }

    @Override
    public void kick(String reason) {
        player.kickPlayer(reason);
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }
}
