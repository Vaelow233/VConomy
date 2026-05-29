package org.vaelow233.vconomy.bukkit.hook.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.vaelow233.vconomy.VConomyPlugin;
import org.vaelow233.vconomy.bukkit.adapter.SpigotOfflinePlayer;

import java.sql.SQLException;

public class VConomyPAPIExpansion extends PlaceholderExpansion {

    private final VConomyPlugin plugin;

    public VConomyPAPIExpansion(VConomyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "Vaelow233";
    }

    @Override
    public String getIdentifier() {
        return "vconomy";
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        String[] parts = params.split("\\.");
        if (parts.length == 1) {
            if (plugin.getEconomyTypes().contains(parts[0])) {
                try {
                    return plugin.getStorage().get(new SpigotOfflinePlayer(player), parts[0]).toPlainString();
                } catch (SQLException e) {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        return onRequest(player, params);
    }
}
