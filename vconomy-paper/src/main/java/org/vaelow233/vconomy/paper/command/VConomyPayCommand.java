package org.vaelow233.vconomy.paper.command;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.vaelow233.vconomy.paper.VConomyPaperPlugin;
import org.vaelow233.vconomy.paper.adapter.PaperCommandSender;
import org.vaelow233.vconomy.paper.adapter.PaperPlayer;

import java.util.Collection;

@SuppressWarnings("UnstableApiUsage")
public class VConomyPayCommand implements BasicCommand {
    private final VConomyPaperPlugin plugin;

    public VConomyPayCommand(VConomyPaperPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSourceStack commandSourceStack, String[] args) {
        if (commandSourceStack.getSender() instanceof Player) {
            plugin.onCommand(plugin, new PaperPlayer((Player) commandSourceStack.getSender()), "pay", args);
        } else {
            plugin.onCommand(plugin, new PaperCommandSender(commandSourceStack.getSender()), "pay", args);
        }
    }

    @Override
    public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args) {
        return plugin.onTabComplete(plugin, new PaperCommandSender(commandSourceStack.getSender()), "pay", args);
    }
}
