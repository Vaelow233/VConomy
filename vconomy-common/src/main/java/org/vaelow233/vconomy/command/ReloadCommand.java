package org.vaelow233.vconomy.command;

import org.vaelow233.vconomy.VConomyPlugin;
import org.vaelow233.vconomy.adapter.VCommandSender;
import org.vaelow233.vconomy.config.VConomyLangConfig;
import org.vaelow233.vconomy.util.LangUtil;
import org.vaelow233.vconomy.util.MapUtil;

public class ReloadCommand {
    public static void execute(VConomyPlugin plugin, VCommandSender sender) {
        VConomyLangConfig langConfig = plugin.getLangConfig();
        if (!sender.hasPermission("vconomy.admin.reload")) {
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.noPermission));
            return;
        }
        try {
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.reloading));
            plugin.reload();
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.operationSuccessful));
        } catch (Exception e) {
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.exception, MapUtil.of("exception", e.getMessage())));
        }
    }
}
