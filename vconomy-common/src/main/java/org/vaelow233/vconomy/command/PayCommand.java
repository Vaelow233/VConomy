package org.vaelow233.vconomy.command;

import org.vaelow233.vconomy.VConomyPlugin;
import org.vaelow233.vconomy.adapter.VCommandSender;
import org.vaelow233.vconomy.adapter.VPlayer;
import org.vaelow233.vconomy.config.VConomyEconomyConfig;
import org.vaelow233.vconomy.config.VConomyLangConfig;
import org.vaelow233.vconomy.util.LangUtil;
import org.vaelow233.vconomy.util.MapUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

public class PayCommand {
    public static void execute(VConomyPlugin plugin, VCommandSender sender, String[] args) {
        try {
            VConomyLangConfig langConfig = plugin.getLangConfig();
            if (!sender.hasPermission("vconomy.pay")) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.noPermission));
                return;
            }
            if (!(sender instanceof VPlayer)) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.onlyPlayerCanExecute));
                return;
            }
            if (sender.getName().equalsIgnoreCase(args[0])) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.cannotPayYourself));
                return;
            }
            if (args.length != 3) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidArguments));
                return;
            }
            Optional<VPlayer> target =
                    plugin.getOnlinePlayers().stream().filter(player -> player.getName().equals(args[0])).findFirst();
            if (!target.isPresent()) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.playerNotFound));
                return;
            }
            Optional<VConomyEconomyConfig> economyConfig = plugin.getEconomyConfigByName(args[1]);
            if (!economyConfig.isPresent()) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidBalanceType));
                return;
            }
            try {
                BigDecimal amount = new BigDecimal(args[2]);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
                BigDecimal cost = amount.add(amount.multiply(BigDecimal.valueOf(economyConfig.get().paymentTax)));
                if (plugin.getStorage().get(sender, args[1]).compareTo(cost) < 0) throw new IllegalArgumentException();
                plugin.getStorage().subtract(sender, sender, args[1], cost);
                plugin.getStorage().add(sender, target.get(), args[1], amount);
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.operationSuccessful));
            } catch (NumberFormatException e) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.invalidBalance));
            } catch (IllegalArgumentException e) {
                sender.sendMessage(LangUtil.formatFromConfig(langConfig.notEnoughBalance));
            }
        } catch (SQLException e) {
            sender.sendMessage(LangUtil.formatFromConfig(plugin.getLangConfig().sqlException, MapUtil.of("exception", e)));
        }
    }
}
