package org.vaelow233.vconomy;

import org.vaelow233.vconomy.adapter.VLogger;
import org.vaelow233.vconomy.adapter.VOfflinePlayer;
import org.vaelow233.vconomy.adapter.VPlayer;
import org.vaelow233.vconomy.command.CommandProvider;
import org.vaelow233.vconomy.config.ConfigProvider;
import org.vaelow233.vconomy.hook.HookProvider;
import org.vaelow233.vconomy.metrics.MetricsProvider;
import org.vaelow233.vconomy.storage.StorageProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface VConomyPlugin extends ConfigProvider, CommandProvider, HookProvider, MetricsProvider, StorageProvider {
    Path getDataDirectory();

    VLogger getVConomyLogger();

    default void enable() {
        try {
            getVConomyLogger().info("Loading config...");
            loadConfig(getDataDirectory());
            getVConomyLogger().info("Loading storage...");
            loadStorage(this);
            getVConomyLogger().info("Registering commands...");
            registerCommands();
            getVConomyLogger().info("Loading hooks...");
            loadHooks(this);
            getVConomyLogger().info("Loading metrics...");
            loadMetrics(this);
        } catch (Exception e) {
            getVConomyLogger().error("Failed to enable the VConomy plugin", e);
            this.disable();
        }
    }

    default void disable() {
        try {
            getVConomyLogger().info("Saving data...");
            closeStorage();
        } catch (Exception e) {
            getVConomyLogger().error("Failed to disable the VConomy plugin", e);
        }
    }

    VOfflinePlayer getOfflinePlayer(String name);

    List<VPlayer> getOnlinePlayers();

    boolean isPluginEnabled(String name);

    default void reload() throws Exception {
        getVConomyLogger().info("Reloading config...");
        loadConfig(getDataDirectory());
        getVConomyLogger().info("Reloading storage...");
        closeStorage();
        loadStorage(this);
        postReload();
    }

    void postReload();
}
