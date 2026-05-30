package org.vaelow233.vconomy.paper.hook.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.vaelow233.vconomy.config.VConomyEconomyConfig;
import org.vaelow233.vconomy.paper.VConomyPaperPlugin;

import java.util.List;

public interface VaultHookProvider {
    default void loadVaultHook(VConomyPaperPlugin plugin) {
        String type = plugin.getGeneralConfig().hook.vault.type;
        if (type.isEmpty()) return;
        List<VConomyEconomyConfig> filtered = plugin.getEconomyConfigs().stream()
                .filter(config -> config.name.equalsIgnoreCase(type))
                .toList();
        if (filtered.size() != 1) {
            plugin.getLogger().warning("Invalid vault economy type: " + type);
            return;
        }
        Bukkit.getServicesManager().register(Economy.class, new VaultEconomy(plugin, filtered.getFirst()),
                plugin, ServicePriority.High);
        plugin.getLogger().info("Registered Vault economy: " + filtered.getFirst().name);
    }

    default void reloadVaultHook(VConomyPaperPlugin plugin) {
        Bukkit.getServicesManager().unregister(plugin);
        loadVaultHook(plugin);
    }
}
