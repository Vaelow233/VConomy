package org.vaelow233.vconomy.storage.executor;

import org.vaelow233.vconomy.adapter.VOfflinePlayer;
import org.vaelow233.vconomy.config.VConomyEconomyConfig;
import org.vaelow233.vconomy.storage.action.SqlActionType;

import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class H2Executor extends DatabaseExecutor {
    private static final String ADD_PLAYER_BALANCE = "MERGE INTO `vconomy_balance` AS t\n" +
            "USING (SELECT CAST(? AS VARCHAR(36)) AS `uuid`, CAST(? AS VARCHAR(64)) AS `name`," +
            " CAST(? AS DECIMAL(28, 4)) AS `seed`, CAST(? AS DECIMAL(28, 4)) AS `delta`) AS s\n" +
            "  ON t.`uuid` = s.`uuid`\n" +
            "WHEN MATCHED THEN\n" +
            "  UPDATE SET t.`%1$s` = t.`%1$s` + s.`delta`\n" +
            "WHEN NOT MATCHED THEN\n" +
            "  INSERT (`uuid`, `name`, `%1$s`) VALUES (s.`uuid`, s.`name`, s.`seed`)";

    private static final String SUBTRACT_PLAYER_BALANCE = "MERGE INTO `vconomy_balance` AS t\n" +
            "USING (SELECT CAST(? AS VARCHAR(36)) AS `uuid`, CAST(? AS VARCHAR(64)) AS `name`," +
            " CAST(? AS DECIMAL(28, 4)) AS `seed`, CAST(? AS DECIMAL(28, 4)) AS `delta`) AS s\n" +
            "  ON t.`uuid` = s.`uuid`\n" +
            "WHEN MATCHED THEN\n" +
            "  UPDATE SET t.`%1$s` = t.`%1$s` - s.`delta`\n" +
            "WHEN NOT MATCHED THEN\n" +
            "  INSERT (`uuid`, `name`, `%1$s`) VALUES (s.`uuid`, s.`name`, s.`seed`)";

    private static final String SET_PLAYER_BALANCE = "MERGE INTO `vconomy_balance` AS t\n" +
            "USING (SELECT CAST(? AS VARCHAR(36)) AS `uuid`, CAST(? AS VARCHAR(64)) AS `name`, CAST(? AS DECIMAL(28, 4)) AS `val`) AS s\n" +
            "  ON t.`uuid` = s.`uuid`\n" +
            "WHEN MATCHED THEN\n" +
            "  UPDATE SET t.`%1$s` = s.`val`\n" +
            "WHEN NOT MATCHED THEN\n" +
            "  INSERT (`uuid`, `name`, `%1$s`) VALUES (s.`uuid`, s.`name`, s.`val`)";

    private static final String RESET_PLAYER_BALANCE = "MERGE INTO `vconomy_balance` AS t\n" +
            "USING (SELECT CAST(? AS VARCHAR(36)) AS `uuid`, CAST(? AS VARCHAR(64)) AS `name`) AS s\n" +
            "  ON t.`uuid` = s.`uuid`\n" +
            "WHEN MATCHED THEN\n" +
            "  UPDATE SET t.`%1$s` = 0\n" +
            "WHEN NOT MATCHED THEN\n" +
            "  INSERT (`uuid`, `name`, `%1$s`) VALUES (s.`uuid`, s.`name`, 0)";

    private static final String DELETE_PLAYER_DATA = "DELETE FROM `vconomy_balance` WHERE `uuid` = ?";

    private static final String QUERY_PLAYER_BALANCE = "SELECT `%1$s` FROM `vconomy_balance` WHERE `uuid` = ? LIMIT 1";

    private static final String QUERY_BALANCE_TOP = "SELECT `%1$s`, name FROM `vconomy_balance` ORDER BY `%1$s` DESC LIMIT ?";

    private static final String CHANGE_PLAYER_BALANCE_RECORD = "INSERT INTO `vconomy_record` " +
            "(`operator_uuid`, `operator_name`, `target_uuid`, `target_name`, `operation`, `balance_type`, `balance`) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String CREATE_BALANCE_TABLE = "CREATE TABLE IF NOT EXISTS `vconomy_balance` (\n"
            + "  `uuid` VARCHAR(36) PRIMARY KEY,\n"
            + "  `name` VARCHAR(64) NOT NULL\n"
            + ")";

    private static final String CREATE_RECORD_TABLE = "CREATE TABLE IF NOT EXISTS `vconomy_record` (\n"
            + "  `id` INT AUTO_INCREMENT PRIMARY KEY,\n"
            + "  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n"
            + "  `operator_uuid` VARCHAR(36),\n"
            + "  `operator_name` VARCHAR(64) NOT NULL,\n"
            + "  `target_uuid` VARCHAR(36) NOT NULL,\n"
            + "  `target_name` VARCHAR(64) NOT NULL,\n"
            + "  `operation` TINYINT NOT NULL,\n"
            + "  `balance_type` VARCHAR(16) NOT NULL,\n"
            + "  `balance` DECIMAL(28, 4) NOT NULL\n"
            + ")";

    private static final String BALANCE_ADD_COLUMN = "ALTER TABLE `vconomy_balance` ADD COLUMN `%1$s` DECIMAL(%2$d, %3$d) " +
            "NOT NULL DEFAULT %4$s";

    @Override
    public void createDefaultTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.addBatch(CREATE_BALANCE_TABLE);
            statement.addBatch(CREATE_RECORD_TABLE);
            statement.executeBatch();
        }
    }

    @Override
    public void updateTableDesc(Connection connection, List<VConomyEconomyConfig> configs) throws SQLException {
        for (VConomyEconomyConfig config : configs) {
            checkBalanceTypeName(config.name);
            BigDecimal initial = parseInitialBalance(config);
            if (columnExists(connection, "vconomy_balance", config.name)) {
                continue;
            }
            try (Statement statement = connection.createStatement()) {
                statement.execute(String.format(BALANCE_ADD_COLUMN,
                        config.name, config.totalDigit, config.decimalDigit, toSqlLiteral(initial)));
            }
        }
    }

    @Override
    public void addRecord(Connection connection, VOfflinePlayer operator, VOfflinePlayer target, SqlActionType type,
                          String balanceType, BigDecimal balance) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                CHANGE_PLAYER_BALANCE_RECORD
        )) {
            preparedStatement.setString(1, operator.getUuid() == null ? null : operator.getUuid().toString());
            preparedStatement.setString(2, operator.getName());
            preparedStatement.setString(3, target.getUuid().toString());
            preparedStatement.setString(4, target.getName());
            preparedStatement.setByte(5, type.getId());
            preparedStatement.setString(6, balanceType);
            preparedStatement.setBigDecimal(7, balance);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void addBalance(Connection connection, VOfflinePlayer target, String balanceType,
                           BigDecimal balance, BigDecimal initialBalance) throws SQLException {
        checkBalanceTypeName(balanceType);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                String.format(ADD_PLAYER_BALANCE, balanceType)
        )) {
            preparedStatement.setString(1, target.getUuid().toString());
            preparedStatement.setString(2, target.getName());
            preparedStatement.setBigDecimal(3, initialBalance.add(balance));
            preparedStatement.setBigDecimal(4, balance);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void subtractBalance(Connection connection, VOfflinePlayer target, String balanceType,
                                BigDecimal balance, BigDecimal initialBalance) throws SQLException {
        checkBalanceTypeName(balanceType);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                String.format(SUBTRACT_PLAYER_BALANCE, balanceType)
        )) {
            preparedStatement.setString(1, target.getUuid().toString());
            preparedStatement.setString(2, target.getName());
            preparedStatement.setBigDecimal(3, initialBalance.subtract(balance));
            preparedStatement.setBigDecimal(4, balance);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void setBalance(Connection connection, VOfflinePlayer target, String balanceType, BigDecimal balance) throws SQLException {
        checkBalanceTypeName(balanceType);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                String.format(SET_PLAYER_BALANCE, balanceType)
        )) {
            preparedStatement.setString(1, target.getUuid().toString());
            preparedStatement.setString(2, target.getName());
            preparedStatement.setBigDecimal(3, balance);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void resetBalance(Connection connection, VOfflinePlayer target, String balanceType) throws SQLException {
        checkBalanceTypeName(balanceType);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                String.format(RESET_PLAYER_BALANCE, balanceType)
        )) {
            preparedStatement.setString(1, target.getUuid().toString());
            preparedStatement.setString(2, target.getName());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void resetData(Connection connection, VOfflinePlayer target) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                DELETE_PLAYER_DATA
        )) {
            preparedStatement.setString(1, target.getUuid().toString());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public BigDecimal getBalance(Connection connection, VOfflinePlayer target, String balanceType,
                                 BigDecimal initialBalance) throws SQLException {
        checkBalanceTypeName(balanceType);
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                String.format(QUERY_PLAYER_BALANCE, balanceType)
        )) {
            preparedStatement.setString(1, target.getUuid().toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    BigDecimal decimal = resultSet.getBigDecimal(1);
                    return decimal == null ? initialBalance : decimal;
                }
                return initialBalance;
            }
        }
    }

    @Override
    public Map<String, BigDecimal> getBalanceTop(Connection connection, String balanceType, int page) throws SQLException {
        checkBalanceTypeName(balanceType);
        Map<String, BigDecimal> map = new LinkedHashMap<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                String.format(QUERY_BALANCE_TOP, balanceType)
        )) {
            preparedStatement.setInt(1, page * 10);
            int i = 0;
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    if (i >= (page - 1) * 10 && i < page * 10) {
                        BigDecimal decimal = resultSet.getBigDecimal(1);
                        String name = resultSet.getString(2);
                        map.put(name, decimal);
                    }
                    i++;
                }
            }
        }
        return map;
    }
}
