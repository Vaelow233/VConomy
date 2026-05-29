package org.vaelow233.vconomy.storage.executor;

import org.vaelow233.vconomy.adapter.VOfflinePlayer;
import org.vaelow233.vconomy.config.VConomyEconomyConfig;
import org.vaelow233.vconomy.storage.action.SqlActionType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class DatabaseExecutor {
    protected DatabaseExecutor() {}
    
    public abstract void createDefaultTable(Connection connection) throws SQLException;
    public abstract void updateTableDesc(Connection connection, List<VConomyEconomyConfig> configs) throws SQLException;
    
    public abstract void addRecord(Connection connection, VOfflinePlayer operator, VOfflinePlayer target, SqlActionType type,
                                   String balanceType, BigDecimal balance) throws SQLException;
    
    public abstract void addBalance(Connection connection, VOfflinePlayer target, String balanceType,
                                    BigDecimal balance, BigDecimal initialBalance) throws SQLException;
    public abstract void subtractBalance(Connection connection, VOfflinePlayer target, String balanceType,
                                         BigDecimal balance, BigDecimal initialBalance) throws SQLException;
    public abstract void setBalance(Connection connection, VOfflinePlayer target, String balanceType, BigDecimal balance) throws SQLException;
    public abstract void resetBalance(Connection connection, VOfflinePlayer target, String balanceType) throws SQLException;
    public abstract void resetData(Connection connection, VOfflinePlayer target) throws SQLException;
    public abstract BigDecimal getBalance(Connection connection, VOfflinePlayer target, String balanceType,
                                          BigDecimal initialBalance) throws SQLException;
    public abstract Map<String, BigDecimal> getBalanceTop(Connection connection, String balanceType, int page) throws SQLException;

    public boolean columnExists(Connection conn, String tableName, String columnName) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getColumns(null, null, null, null)) {
            while (rs.next()) {
                String currentTable = rs.getString("TABLE_NAME");
                String currentColumn = rs.getString("COLUMN_NAME");
                if (currentTable != null && currentColumn != null
                        && currentTable.equalsIgnoreCase(tableName)
                        && currentColumn.equalsIgnoreCase(columnName)) {
                    return true;
                }
            }
            return false;
        }
    }

    protected void checkBalanceTypeName(String balanceType) {
        if (!balanceType.matches("^[A-Za-z_][A-Za-z0-9_]{0,31}$")) {
            throw new IllegalArgumentException("Invalid balance type name: " + balanceType);
        }
    }

    protected static BigDecimal parseInitialBalance(VConomyEconomyConfig config) {
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
            throw new IllegalArgumentException(
                    "Invalid initial_balance for economy '" + config.name + "': " + raw, e);
        }
    }

    protected static String toSqlLiteral(BigDecimal value) {
        String plain = value.toPlainString();
        if (value.signum() < 0) {
            return "(" + plain + ")";
        }
        return plain;
    }
}
