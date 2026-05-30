package org.vaelow233.vconomy.paper;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaelow233.vconomy.VConomyPlugin;
import org.vaelow233.vconomy.adapter.VLogger;
import org.vaelow233.vconomy.adapter.VOfflinePlayer;
import org.vaelow233.vconomy.adapter.VPlayer;
import org.vaelow233.vconomy.config.VConomyEconomyConfig;
import org.vaelow233.vconomy.config.VConomyGeneralConfig;
import org.vaelow233.vconomy.config.VConomyLangConfig;
import org.vaelow233.vconomy.paper.adapter.PaperLogger;
import org.vaelow233.vconomy.paper.adapter.PaperOfflinePlayer;
import org.vaelow233.vconomy.paper.adapter.PaperPlayer;
import org.vaelow233.vconomy.paper.bstats.Metrics;
import org.vaelow233.vconomy.paper.command.VConomyBalanceCommand;
import org.vaelow233.vconomy.paper.command.VConomyCommand;
import org.vaelow233.vconomy.paper.command.VConomyPayCommand;
import org.vaelow233.vconomy.paper.command.VConomyTopCommand;
import org.vaelow233.vconomy.paper.hook.PAPIHookProvider;
import org.vaelow233.vconomy.paper.hook.vault.VaultHookProvider;
import org.vaelow233.vconomy.storage.Storage;

import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class VConomyPaperPlugin extends JavaPlugin implements VConomyPlugin, VaultHookProvider, PAPIHookProvider {

    private Storage storage;
    private URLClassLoader classLoader;
    private final VLogger logger = new PaperLogger(getSLF4JLogger());
    private VConomyGeneralConfig generalConfig;
    private VConomyLangConfig langConfig;
    private List<VConomyEconomyConfig> economyConfigs;

    @Override
    public Storage getStorage() {
        return storage;
    }

    @Override
    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void setCustomClassLoader(URLClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public URLClassLoader getCustomClassLoader() {
        return classLoader;
    }

    @Override
    public Path getDataDirectory() {
        return getDataFolder().toPath();
    }

    @Override
    public VLogger getVConomyLogger() {
        return logger;
    }

    @Override
    public void setGeneralConfig(VConomyGeneralConfig generalConfig) {
        this.generalConfig = generalConfig;

    }

    @Override
    public VConomyGeneralConfig getGeneralConfig() {
        return generalConfig;
    }

    @Override
    public void setLangConfig(VConomyLangConfig langConfig) {
        this.langConfig = langConfig;

    }

    @Override
    public VConomyLangConfig getLangConfig() {
        return langConfig;
    }

    @Override
    public void setEconomyConfigs(List<VConomyEconomyConfig> economyConfigs) {
        this.economyConfigs = economyConfigs;

    }

    @Override
    public List<VConomyEconomyConfig> getEconomyConfigs() {
        return economyConfigs;
    }

    @Override
    public VOfflinePlayer getOfflinePlayer(String name) {
        return new PaperOfflinePlayer(getServer().getOfflinePlayer(name));
    }

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, events -> {
            VConomyCommand vConomyCommand = new VConomyCommand(this);
            events.registrar().register("vconomy", generalConfig.command.aliases.vconomy, vConomyCommand);
            VConomyTopCommand vConomyTopCommand = new VConomyTopCommand(this);
            events.registrar().register("balancetop", generalConfig.command.aliases.balancetop, vConomyTopCommand);
            VConomyBalanceCommand vConomyBalanceCommand = new VConomyBalanceCommand(this);
            events.registrar().register("balance", generalConfig.command.aliases.balance, vConomyBalanceCommand);
            VConomyPayCommand vConomyPayCommand = new VConomyPayCommand(this);
            events.registrar().register("pay", generalConfig.command.aliases.pay, vConomyPayCommand);
        });
    }

    @Override
    public void saveResource(String path) {
        saveResource(path, false);
    }

    @Override
    public void loadHooks(VConomyPlugin plugin) {
        if (isPluginEnabled("Vault")) {
            getLogger().info("Loading Vault hooks...");
            loadVaultHook(this);
        }
        if (isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("Loading PlaceholderAPI hooks...");
            loadPAPIHook(this);
        }
    }

    @Override
    public void postReload() {
        if (isPluginEnabled("Vault")) {
            getLogger().info("Reloading Vault hooks...");
            reloadVaultHook(this);
        }
    }

    @Override
    public void loadMetrics(VConomyPlugin plugin) {
        int pluginId = 31629;
        Metrics metrics = new Metrics(this, pluginId);
    }

    @Override
    public void onEnable() {
        this.enable();
    }

    @Override
    public void onDisable() {
        this.disable();
    }

    @Override
    public List<VPlayer> getOnlinePlayers() {
        return getServer().getOnlinePlayers().stream()
                .map(PaperPlayer::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isPluginEnabled(String name) {
        return getServer().getPluginManager().isPluginEnabled(name);
    }
}
