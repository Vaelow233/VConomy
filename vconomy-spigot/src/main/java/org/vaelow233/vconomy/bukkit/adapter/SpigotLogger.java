package org.vaelow233.vconomy.bukkit.adapter;

import org.vaelow233.vconomy.adapter.VLogger;
import org.vaelow233.vconomy.bukkit.VConomyBukkitPlugin;

public class SpigotLogger implements VLogger {

    private final VConomyBukkitPlugin plugin;

    public SpigotLogger(VConomyBukkitPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void info(String msg) {
        plugin.getLogger().info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        plugin.getLogger().info(String.format(format, arg));

    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        plugin.getLogger().info(String.format(format, arg1, arg2));

    }

    @Override
    public void info(String format, Object... arguments) {
        plugin.getLogger().info(String.format(format, arguments));
    }

    @Override
    public void info(String msg, Throwable t) {
        plugin.getLogger().info(msg + ": " + t);

    }

    @Override
    public void warn(String msg) {
        plugin.getLogger().warning(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        plugin.getLogger().warning(String.format(format, arg));

    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        plugin.getLogger().warning(String.format(format, arg1, arg2));
    }

    @Override
    public void warn(String format, Object... arguments) {
        plugin.getLogger().warning(String.format(format, arguments));
    }

    @Override
    public void warn(String msg, Throwable t) {
        plugin.getLogger().warning(msg + ": " + t);
    }

    @Override
    public void error(String msg) {
        plugin.getLogger().severe(msg);
    }

    @Override
    public void error(String format, Object arg) {
        plugin.getLogger().severe(String.format(format, arg));
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        plugin.getLogger().severe(String.format(format, arg1, arg2));
    }

    @Override
    public void error(String format, Object... arguments) {
        plugin.getLogger().severe(String.format(format, arguments));
    }

    @Override
    public void error(String msg, Throwable t) {
        plugin.getLogger().severe(msg + ": " + t);
    }
}
