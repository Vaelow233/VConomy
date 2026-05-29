package org.vaelow233.vconomy.command;

import org.vaelow233.vconomy.VConomyPlugin;
import org.vaelow233.vconomy.adapter.VCommandSender;
import org.vaelow233.vconomy.config.VConomyEconomyConfig;
import org.vaelow233.vconomy.config.VConomyLangConfig;
import org.vaelow233.vconomy.util.LangUtil;
import org.vaelow233.vconomy.util.MapUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class TopCommand {
    public static void execute(VConomyPlugin plugin, VCommandSender sender, String[] args) {
        try {
            VConomyLangConfig langConfig = plugin.getLangConfig();
            if (!sender.hasPermission("vconomy.top")) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.noPermission));
                return;
            }
            if (args.length != 1 && args.length != 2) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidArguments));
                return;
            }
            Optional<VConomyEconomyConfig> economyConfig = plugin.getEconomyConfigByName(args[0]);
            if (!economyConfig.isPresent()) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidBalanceType));
                return;
            }
            int page = 1;
            if (args.length == 2) {
                try {
                    page = Integer.parseInt(args[1]);
                    if (page <= 0) throw new IllegalArgumentException();
                } catch (Exception e) {
                    sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidArguments));
                    return;
                }
            }
            Map<String, BigDecimal> map = plugin.getStorage().top(args[0], page);
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.balanceTop.title, MapUtil.of(
                    "type", args[0],
                    "page", page
            )));
            for (Map.Entry<String, BigDecimal> entry : map.entrySet()) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.balanceTop.line, MapUtil.of(
                        "player", entry.getKey(),
                        "balance", LangUtil.formatFromConfig(economyConfig.get().display, MapUtil.of(
                                "balance", entry.getValue()
                        ))
                )));
            }
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.balanceTop.footer, MapUtil.of(
                    "type", args[0],
                    "page", page
            )));
        } catch (SQLException e) {
            sender.sendMessage(LangUtil.formatFromConfig(plugin.getLangConfig().sqlException, MapUtil.of("exception", e)));
        }
    }
}
