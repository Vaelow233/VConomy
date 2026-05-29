package org.vaelow233.vconomy.bukkit.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.vaelow233.vconomy.bukkit.VConomyBukkitPlugin;
import org.vaelow233.vconomy.bukkit.adapter.SpigotCommandSender;
import org.vaelow233.vconomy.bukkit.adapter.SpigotPlayer;

import java.util.List;

public class VConomyBalanceCommand implements CommandExecutor, TabCompleter {
    private final VConomyBukkitPlugin plugin;

    public VConomyBalanceCommand(VConomyBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            plugin.onCommand(plugin, new SpigotPlayer((Player) sender), "balance", args);
        } else {
            plugin.onCommand(plugin, new SpigotCommandSender(sender), "balance", args);
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return plugin.onTabComplete(plugin, new SpigotCommandSender(sender), "balance", args);
    }
}