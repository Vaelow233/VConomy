package org.vaelow233.vconomy.command;

import org.vaelow233.vconomy.VConomyPlugin;
import org.vaelow233.vconomy.adapter.VCommandSender;
import org.vaelow233.vconomy.adapter.VOfflinePlayer;
import org.vaelow233.vconomy.config.VConomyLangConfig;
import org.vaelow233.vconomy.util.LangUtil;
import org.vaelow233.vconomy.util.MapUtil;

import java.sql.SQLException;
import java.util.Map;

public class ResetCommand {
    public static void execute(VConomyPlugin plugin, VCommandSender sender, String[] args) {
        try {
            VConomyLangConfig langConfig = plugin.getLangConfig();
            if (!sender.hasPermission("vconomy.admin.reset")) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.noPermission));
                return;
            }
            if (args.length != 2 && args.length != 3) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidArguments));
                return;
            }
            VOfflinePlayer player = plugin.getOfflinePlayer(args[1]);
            if (args.length == 3) {
                if (!plugin.getEconomyConfigByName(args[2]).isPresent()) {
                    sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidBalanceType));
                    return;
                }
                plugin.getStorage().reset(sender, player, args[2]);
            } else {
                plugin.getStorage().resetAll(sender, player);
            }
            sender.sendMessage(LangUtil.formatFromConfig(plugin.getLangConfig().operationSuccessful));
        } catch (SQLException e) {
            sender.sendMessage(LangUtil.formatFromConfig(plugin.getLangConfig().sqlException, MapUtil.of("exception", e)));
        }
    }
}
