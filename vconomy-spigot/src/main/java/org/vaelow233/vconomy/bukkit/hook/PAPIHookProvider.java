package org.vaelow233.vconomy.bukkit.hook;

import org.vaelow233.vconomy.bukkit.VConomyBukkitPlugin;
import org.vaelow233.vconomy.bukkit.hook.papi.VConomyPAPIExpansion;

public interface PAPIHookProvider {

    default void loadPAPIHook(VConomyBukkitPlugin plugin) {
        new VConomyPAPIExpansion(plugin).register();
    }
}
