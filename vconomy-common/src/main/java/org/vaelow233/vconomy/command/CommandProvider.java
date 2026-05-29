package org.vaelow233.vconomy.command;

import org.vaelow233.vconomy.VConomyPlugin;
import org.vaelow233.vconomy.adapter.VCommandSender;
import org.vaelow233.vconomy.adapter.VPlayer;
import org.vaelow233.vconomy.util.ListUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface CommandProvider {
    void registerCommands();

    default void onCommand(VConomyPlugin plugin, VCommandSender sender, String command, String[] args) {
        if (command.equalsIgnoreCase("vconomy")) {
            Optional<VPlayer> player = Optional.empty();
            if (sender instanceof VPlayer) {
                player = Optional.of((VPlayer) sender);
            }
            if (args.length == 0) {
                HelpCommand.execute(plugin, player.isPresent() ? player.get() : sender);
            } else {
                switch (args[0]) {
                    case "help": {
                        HelpCommand.execute(plugin, player.isPresent() ? player.get() : sender);
                        break;
                    }
                    case "give":
                    case "add": {
                        GiveCommand.execute(plugin, player.isPresent() ? player.get() : sender, args);
                        break;
                    }
                    case "take": {
                        TakeCommand.execute(plugin, player.isPresent() ? player.get() : sender, args);
                        break;
                    }
                    case "set": {
                        SetCommand.execute(plugin, player.isPresent() ? player.get() : sender, args);
                        break;
                    }
                    case "reset": {
                        ResetCommand.execute(plugin, player.isPresent() ? player.get() : sender, args);
                        break;
                    }
                    case "get":
                    case "show": {
                        GetCommand.execute(plugin, player.isPresent() ? player.get() : sender, args);
                        break;
                    }
                    case "top": {
                        String[] handledArgs = Arrays.copyOfRange(args, 1, args.length);
                        TopCommand.execute(plugin, player.isPresent() ? player.get() : sender, handledArgs);
                        break;
                    }
                    case "reload": {
                        ReloadCommand.execute(plugin, player.isPresent() ? player.get() : sender);
                        break;
                    }
                }
            }
        } else if (command.equalsIgnoreCase("balancetop")) {
            TopCommand.execute(plugin, sender, args);
        } else if (command.equalsIgnoreCase("balance")) {
            String[] handledArgs = new String[args.length + 1];
            handledArgs[0] = "get";
            System.arraycopy(args, 0, handledArgs, 1, args.length);
            GetCommand.execute(plugin, sender, handledArgs);
        } else if (command.equalsIgnoreCase("pay")) {
            PayCommand.execute(plugin, sender, args);
        }
    }

    default List<String> onTabComplete(VConomyPlugin plugin, VCommandSender sender, String command, String[] args) {
        if (command.equalsIgnoreCase("vconomy")) {
            if (args.length == 1) {
                List<String> list = new ArrayList<>();
                list.add("help");
                for (String s : ListUtil.of("give", "take", "set", "reset", "get", "top", "reload")) {
                    switch (s) {
                        case "give":
                        case "set":
                        case "take":
                        case "reset":
                        case "reload": {
                            if (sender.hasPermission("vconomy.admin." + s)) {
                                list.add(s);
                            }
                            break;
                        }
                        case "get":
                        case "top": {
                            if (sender.hasPermission("vconomy." + s)) {
                                list.add(s);
                            }
                            break;
                        }
                    }
                }
                return list;
            } else if (args.length == 2) {
                switch (args[0]) {
                    case "give":
                    case "add":
                    case "set":
                    case "take":
                    case "reset": {
                        if (sender.hasPermission("vconomy.admin." + args[0])) {
                            return plugin.getOnlinePlayers().stream().map(VPlayer::getName).collect(Collectors.toList());
                        }
                        return new ArrayList<>();
                    }
                    case "get":
                    case "show": {
                        if (sender.hasPermission("vconomy.get")) {
                            return plugin.getEconomyTypes();
                        }
                        return new ArrayList<>();
                    }
                    case "top": {
                        if (sender.hasPermission("vconomy.top")) {
                            return plugin.getEconomyTypes();
                        }
                        return new ArrayList<>();
                    }
                }
            } else if (args.length == 3) {
                switch (args[0]) {
                    case "give":
                    case "add":
                    case "set":
                    case "take":
                    case "reset": {
                        if (sender.hasPermission("vconomy.admin." + args[0])) {
                            return plugin.getEconomyTypes();
                        }
                        return new ArrayList<>();
                    }
                    case "get":
                    case "show": {
                        if (sender.hasPermission("vconomy.get.others")) {
                            return plugin.getOnlinePlayers().stream().map(VPlayer::getName).collect(Collectors.toList());
                        }
                        return new ArrayList<>();
                    }
                }
            }
        } else if (command.equalsIgnoreCase("balancetop")) {
            if (!sender.hasPermission("vconomy.top")) {
                return new ArrayList<>();
            }
            if (args.length == 1) {
                return plugin.getEconomyTypes();
            }
        } else if (command.equalsIgnoreCase("balance")) {
            if (!sender.hasPermission("vconomy.get")) {
                return new ArrayList<>();
            }
            if (args.length == 1) {
                return plugin.getEconomyTypes();
            } else if (args.length == 2) {
                if (sender.hasPermission("vconomy.get.others")) {
                    return plugin.getOnlinePlayers().stream().map(VPlayer::getName).collect(Collectors.toList());
                }
            }
        } else if (command.equalsIgnoreCase("pay")) {
            if (!sender.hasPermission("vconomy.pay")) {
                return new ArrayList<>();
            }
            if (args.length == 1) {
                return plugin.getOnlinePlayers().stream().map(VPlayer::getName).collect(Collectors.toList());
            } else if (args.length == 2) {
                return plugin.getEconomyTypes();
            }
        }
        return new ArrayList<>();
    }
}
