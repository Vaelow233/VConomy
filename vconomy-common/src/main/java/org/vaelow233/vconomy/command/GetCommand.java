package org.vaelow233.vconomy.command;

import org.vaelow233.vconomy.VConomyPlugin;
import org.vaelow233.vconomy.adapter.VCommandSender;
import org.vaelow233.vconomy.adapter.VPlayer;
import org.vaelow233.vconomy.config.VConomyEconomyConfig;
import org.vaelow233.vconomy.config.VConomyLangConfig;
import org.vaelow233.vconomy.util.LangUtil;
import org.vaelow233.vconomy.util.MapUtil;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class GetCommand {
    public static void execute(VConomyPlugin plugin, VCommandSender sender, String[] args) {
        VConomyLangConfig langConfig = plugin.getLangConfig();
        if (args.length == 2) {
            if (!sender.hasPermission("vconomy.get")) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.noPermission));
                return;
            }
            if (!(sender instanceof VPlayer)) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.onlyPlayerCanExecute));
                return;
            }
        } else if (args.length == 3) {
            if (!sender.hasPermission("vconomy.get.others")) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.noPermission));
                return;
            }
        } else {
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidArguments));
            return;
        }
        Optional<VConomyEconomyConfig> economyConfig = plugin.getEconomyConfigByName(args[1]);
        if (!economyConfig.isPresent()) {
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidBalanceType));
            return;
        }
        try {
            if (args.length == 2) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.balanceShow, MapUtil.of(
                        "type", args[1],
                        "balance", LangUtil.formatFromConfig(economyConfig.get().display, MapUtil.of(
                                "balance", plugin.getStorage().get(sender, args[1])
                        ))))
                );
            } else {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.othersBalanceShow, MapUtil.of(
                        "player", args[2],
                        "type", args[1],
                        "balance", LangUtil.formatFromConfig(economyConfig.get().display, MapUtil.of(
                                "balance", plugin.getStorage().get(plugin.getOfflinePlayer(args[2]), args[1])
                        ))))
                );
            }
        } catch (Exception e) {
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidBalance));
        }
    }
}
