package org.vaelow233.vconomy.paper.adapter;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.vaelow233.vconomy.adapter.VPlayer;

public class PaperPlayer extends VPlayer {

    private final Player player;

    public PaperPlayer(Player player) {
        super(player.getUniqueId(), player.getName());
        this.player = player;
    }

    @Override
    public void kick(String reason) {
        player.kick(Component.text(reason));
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(Component.text(message));
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }
}