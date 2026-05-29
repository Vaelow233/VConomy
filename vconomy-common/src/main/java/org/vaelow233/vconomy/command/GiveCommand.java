package org.vaelow233.vconomy.command;

import org.vaelow233.vconomy.VConomyPlugin;
import org.vaelow233.vconomy.adapter.VCommandSender;
import org.vaelow233.vconomy.adapter.VOfflinePlayer;
import org.vaelow233.vconomy.config.VConomyLangConfig;
import org.vaelow233.vconomy.util.LangUtil;
import org.vaelow233.vconomy.util.MapUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

public class GiveCommand {
    public static void execute(VConomyPlugin plugin, VCommandSender sender, String[] args) {
        VConomyLangConfig langConfig = plugin.getLangConfig();
        if (!sender.hasPermission("vconomy.admin.give")) {
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.noPermission));
            return;
        }
        if (args.length != 4) {
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidArguments));
            return;
        }
        VOfflinePlayer player = plugin.getOfflinePlayer(args[1]);
        if (!plugin.getEconomyConfigByName(args[2]).isPresent()) {
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidBalanceType));
            return;
        }
        try {
            BigDecimal balance = new BigDecimal(args[3]);
            plugin.getStorage().add(sender, player, args[2], balance);
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.operationSuccessful));
        } catch (NumberFormatException e) {
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidBalance));
        } catch (SQLException e) {
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.sqlException, MapUtil.of("exception", e)));
        }
    }
}
