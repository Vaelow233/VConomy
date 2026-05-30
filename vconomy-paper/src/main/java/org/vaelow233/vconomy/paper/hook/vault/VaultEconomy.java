package org.vaelow233.vconomy.paper.hook.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.vaelow233.vconomy.VConomyPlugin;
import org.vaelow233.vconomy.adapter.VOfflinePlayer;
import org.vaelow233.vconomy.config.VConomyEconomyConfig;
import org.vaelow233.vconomy.util.LangUtil;
import org.vaelow233.vconomy.util.MapUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class VaultEconomy implements Economy {

    private final VConomyPlugin plugin;
    private final VConomyEconomyConfig economyConfig;

    public VaultEconomy(VConomyPlugin plugin, VConomyEconomyConfig vaultConfig) {
        this.plugin = plugin;
        this.economyConfig = vaultConfig;
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String name) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(String name, String world) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String world) {
        return true;
    }

    @Override
    public String currencyNamePlural() {
        return economyConfig.pluralName;
    }

    @Override
    public String currencyNameSingular() {
        return economyConfig.name;
    }

    @Override
    public String format(double sum) {
        return LangUtil.formatFromConfig(economyConfig.display, MapUtil.of("balance", sum));
    }

    @Override
    public int fractionalDigits() {
        return economyConfig.decimalDigit;
    }

    @Override
    public boolean hasAccount(String name) {
        return true;
    }

    @Override
    public boolean hasAccount(String name, String world) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String world) {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "VConomy (" + economyConfig.name + ")";
    }

    @Override
    public EconomyResponse depositPlayer(String name, double amount) {
        try {
            plugin.getStorage().add(
                    VOfflinePlayer.ofNonPlayer("VaultAPI"),
                    plugin.getOfflinePlayer(name),
                    economyConfig.name,
                    BigDecimal.valueOf(amount)
            );
            return new EconomyResponse(amount, getBalance(name), EconomyResponse.ResponseType.SUCCESS, "");
        } catch (SQLException e) {
            return new EconomyResponse(amount, getBalance(name), EconomyResponse.ResponseType.FAILURE, e.getMessage());
        }
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return depositPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String name, String world, double amount) {
        return depositPlayer(name, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public double getBalance(String name) {
        try {
            return plugin.getStorage().get(
                    plugin.getOfflinePlayer(name),
                    economyConfig.name
            ).doubleValue();
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return getBalance(player.getName());
    }

    @Override
    public double getBalance(String name, String world) {
        return getBalance(name);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String name, double amount) {
        return getBalance(name) >= amount;
    }

    @Override
    public boolean has(String name, String world, double amount) {
        return has(name, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, String world, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, double amount) {
        try {
            plugin.getStorage().subtract(
                    VOfflinePlayer.ofNonPlayer("VaultAPI"),
                    plugin.getOfflinePlayer(name),
                    economyConfig.name,
                    BigDecimal.valueOf(amount)
            );
            return new EconomyResponse(amount, getBalance(name), EconomyResponse.ResponseType.SUCCESS, "");
        } catch (SQLException e) {
            return new EconomyResponse(amount, getBalance(name), EconomyResponse.ResponseType.FAILURE, e.getMessage());
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdrawPlayer(player.getName(), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, String world, double amount) {
        return withdrawPlayer(name, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {
        return withdrawPlayer(player, amount);
    }
}
