package org.vaelow233.vconomy.bukkit.hook.vault;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.vaelow233.vconomy.bukkit.VConomyBukkitPlugin;
import org.vaelow233.vconomy.config.VConomyEconomyConfig;

import java.util.List;
import java.util.stream.Collectors;

public interface VaultHookProvider {
    default void loadVaultHook(VConomyBukkitPlugin plugin) {
        String type = plugin.getGeneralConfig().hook.vault.type;
        if (type.isEmpty()) return;
        List<VConomyEconomyConfig> filtered = plugin.getEconomyConfigs().stream()
                .filter(config -> config.name.equalsIgnoreCase(type))
                .collect(Collectors.toList());
        if (filtered.size() != 1) {
            plugin.getLogger().warning("Invalid vault economy type: " + type);
            return;
        }
        Bukkit.getServicesManager().register(Economy.class, new VaultEconomy(plugin, filtered.get(0)),
                plugin, ServicePriority.High);
        plugin.getLogger().info("Registered Vault economy: " + filtered.get(0).name);
    }
}
