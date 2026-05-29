package org.vaelow233.vconomy.command;

import org.vaelow233.vconomy.VConomyPlugin;
import org.vaelow233.vconomy.adapter.VCommandSender;
import org.vaelow233.vconomy.config.VConomyLangConfig;
import org.vaelow233.vconomy.util.LangUtil;

public class HelpCommand {
    public static void execute(VConomyPlugin plugin, VCommandSender sender) {
        VConomyLangConfig langConfig = plugin.getLangConfig();
        if (sender.hasPermission("vconomy.admin.help")) {
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.help.admin));
        } else {
            sender.sendMessage(LangUtil.formatFromConfig(langConfig.help.player));
        }
    }
}
