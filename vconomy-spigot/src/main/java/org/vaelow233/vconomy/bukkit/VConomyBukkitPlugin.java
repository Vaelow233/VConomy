package org.vaelow233.vconomy.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.vaelow233.vconomy.VConomyPlugin;
import org.vaelow233.vconomy.adapter.VLogger;
import org.vaelow233.vconomy.adapter.VOfflinePlayer;
import org.vaelow233.vconomy.adapter.VPlayer;
import org.vaelow233.vconomy.bukkit.adapter.SpigotLogger;
import org.vaelow233.vconomy.bukkit.adapter.SpigotOfflinePlayer;
import org.vaelow233.vconomy.bukkit.adapter.SpigotPlayer;
import org.vaelow233.vconomy.bukkit.bstats.Metrics;
import org.vaelow233.vconomy.bukkit.command.VConomyBalanceCommand;
import org.vaelow233.vconomy.bukkit.command.VConomyCommand;
import org.vaelow233.vconomy.bukkit.command.VConomyPayCommand;
import org.vaelow233.vconomy.bukkit.command.VConomyTopCommand;
import org.vaelow233.vconomy.bukkit.hook.PAPIHookProvider;
import org.vaelow233.vconomy.bukkit.hook.vault.VaultHookProvider;
import org.vaelow233.vconomy.config.VConomyEconomyConfig;
import org.vaelow233.vconomy.config.VConomyGeneralConfig;
import org.vaelow233.vconomy.config.VConomyLangConfig;
import org.vaelow233.vconomy.storage.Storage;

import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class VConomyBukkitPlugin extends JavaPlugin implements VConomyPlugin, VaultHookProvider, PAPIHookProvider {

    private Storage storage;
    private URLClassLoader classLoader;
    private final SpigotLogger logger = new SpigotLogger(this);
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
        return new SpigotOfflinePlayer(getServer().getOfflinePlayer(name));
    }

    @Override
    public void registerCommands() {
        PluginCommand vconomyCommand = getCommand("vconomy");
        vconomyCommand.setExecutor(new VConomyCommand(this));
        registerAliasCommands(vconomyCommand, generalConfig.command.aliases.vconomy);
        PluginCommand balancetopCommand = getCommand("balancetop");
        balancetopCommand.setExecutor(new VConomyTopCommand(this));
        registerAliasCommands(balancetopCommand, generalConfig.command.aliases.balancetop);
        PluginCommand balanceCommand = getCommand("balance");
        balanceCommand.setExecutor(new VConomyBalanceCommand(this));
        registerAliasCommands(balanceCommand, generalConfig.command.aliases.balance);
        PluginCommand payCommand = getCommand("pay");
        payCommand.setExecutor(new VConomyPayCommand(this));
        registerAliasCommands(payCommand, generalConfig.command.aliases.pay);
    }

    private void registerAliasCommands(PluginCommand command, List<String> aliases) {
        for (String alias : aliases) {
            registerAliasCommand(command, alias);
        }
    }

    private void registerAliasCommand(PluginCommand command, String alias) {
        String name = alias.trim().toLowerCase(Locale.ROOT);
        CommandMap commandMap = getBukkitCommandMap();
        Command existingCommand = commandMap.getCommand(name);
        if (existingCommand != null) {
            if (existingCommand != command) {
                getLogger().warning("Skipping alias /" + name + " for /" + command.getName() + " because it is already registered");
            }
            return;
        }

        Command aliasCommand = new AliasCommand(this, command, name);
        if (!commandMap.register(getName().toLowerCase(Locale.ROOT), aliasCommand)) {
            aliasCommand.unregister(commandMap);
            getLogger().warning("Skipping alias /" + name + " for /" + command.getName() + " because Bukkit registered it with a fallback prefix");
        }
    }

    private CommandMap getBukkitCommandMap() {
        try {
            Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(getServer());
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to access Bukkit command map", e);
        }
    }

    private static class AliasCommand extends Command implements PluginIdentifiableCommand {
        private final Plugin plugin;
        private final PluginCommand command;

        private AliasCommand(Plugin plugin, PluginCommand command, String alias) {
            super(alias, command.getDescription(), command.getUsage(), Collections.emptyList());
            this.plugin = plugin;
            this.command = command;
            setPermission(command.getPermission());
            setPermissionMessage(command.getPermissionMessage());
        }

        @Override
        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            return command.execute(sender, commandLabel, args);
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
            return command.tabComplete(sender, alias, args);
        }

        @Override
        public Plugin getPlugin() {
            return plugin;
        }
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
                .map(SpigotPlayer::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isPluginEnabled(String name) {
        return getServer().getPluginManager().isPluginEnabled(name);
    }
}
