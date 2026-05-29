package org.vaelow233.vconomy.storage.action;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlAction {
    void run(Connection connection) throws SQLException;
}
