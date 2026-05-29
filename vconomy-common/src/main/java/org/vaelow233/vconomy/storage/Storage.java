package org.vaelow233.vconomy.storage;

import com.zaxxer.hikari.HikariDataSource;
import org.vaelow233.vconomy.adapter.VOfflinePlayer;
import org.vaelow233.vconomy.config.VConomyEconomyConfig;
import org.vaelow233.vconomy.storage.action.SqlAction;
import org.vaelow233.vconomy.storage.action.SqlActionType;
import org.vaelow233.vconomy.storage.executor.DatabaseExecutor;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Storage {
    protected final HikariDataSource dataSource;
    protected final List<VConomyEconomyConfig> configs;
    protected final Map<String, BigDecimal> initialBalances;
    protected final DatabaseExecutor databaseExecutor;

    protected Storage(HikariDataSource dataSource, List<VConomyEconomyConfig> configs, DatabaseExecutor databaseExecutor) {
        this.dataSource = dataSource;
        this.configs = configs;
        this.databaseExecutor = databaseExecutor;
        this.initialBalances = new HashMap<>();
        for (VConomyEconomyConfig config : configs) {
            this.initialBalances.put(config.name, parseInitialBalance(config));
        }
    }

    private static BigDecimal parseInitialBalance(VConomyEconomyConfig config) {
        String raw = config.initialBalance;
        if (raw == null) {
            return BigDecimal.ZERO;
        }
        String trimmed = raw.trim();
        if (trimmed.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(trimmed);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid initial_balance for economy '" + config.name + "': " + raw, e);
        }
    }

    public BigDecimal getInitialBalance(String balanceType) {
        return initialBalances.getOrDefault(balanceType, BigDecimal.ZERO);
    }

    private void run(SqlAction sqlAction) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                sqlAction.run(connection);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public void init() throws SQLException {
        run(connection -> {
            databaseExecutor.createDefaultTable(connection);
            databaseExecutor.updateTableDesc(connection, configs);
        });
    }

    public void add(VOfflinePlayer operator, VOfflinePlayer target, String balanceType, BigDecimal balance) throws SQLException {
        BigDecimal initial = getInitialBalance(balanceType);
        run(connection -> {
            databaseExecutor.addBalance(connection, target, balanceType, balance, initial);
            databaseExecutor.addRecord(connection, operator, target, SqlActionType.OP_ADD, balanceType, balance);
        });
    }

    public void subtract(VOfflinePlayer operator, VOfflinePlayer target, String balanceType, BigDecimal balance) throws SQLException {
        BigDecimal initial = getInitialBalance(balanceType);
        run(connection -> {
            databaseExecutor.subtractBalance(connection, target, balanceType, balance, initial);
            databaseExecutor.addRecord(connection, operator, target, SqlActionType.OP_SUBTRACT, balanceType, balance);
        });
    }

    public void set(VOfflinePlayer operator, VOfflinePlayer target, String balanceType, BigDecimal balance) throws SQLException {
        run(connection -> {
            databaseExecutor.setBalance(connection, target, balanceType, balance);
            databaseExecutor.addRecord(connection, operator, target, SqlActionType.OP_SET, balanceType, balance);
        });
    }

    public void reset(VOfflinePlayer operator, VOfflinePlayer target, String balanceType) throws SQLException {
        run(connection -> {
            databaseExecutor.resetBalance(connection, target, balanceType);
            databaseExecutor.addRecord(connection, operator, target, SqlActionType.OP_RESET, balanceType, BigDecimal.ZERO);
        });
    }

    public void resetAll(VOfflinePlayer operator, VOfflinePlayer target) throws SQLException {
        run(connection -> {
            databaseExecutor.resetData(connection, target);
            databaseExecutor.addRecord(connection, operator, target, SqlActionType.OP_RESET_ALL, "", BigDecimal.ZERO);
        });
    }

    public BigDecimal get(VOfflinePlayer target, String balanceType) throws SQLException {
        BigDecimal initial = getInitialBalance(balanceType);
        try (Connection connection = dataSource.getConnection()) {
            return databaseExecutor.getBalance(connection, target, balanceType, initial);
        }
    }

    public Map<String, BigDecimal> top(String balanceType, int page) throws SQLException {
        try (Connection connection = dataSource.getConnection()){
            return databaseExecutor.getBalanceTop(connection, balanceType, page);
        }
    }
    
    public boolean columnExists(String tableName, String columnName) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            return databaseExecutor.columnExists(connection, tableName, columnName);
        }
    }
}
