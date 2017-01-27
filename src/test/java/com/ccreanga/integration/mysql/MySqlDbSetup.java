package com.ccreanga.integration.mysql;

import com.ccreanga.DbSetup;
import com.ccreanga.MySqlHelper;
import com.ccreanga.jdbc.Dialect;
import com.ccreanga.jdbc.RuntimeSqlException;

import java.sql.Connection;
import java.sql.SQLException;

public class MySqlDbSetup implements DbSetup {

    @Override
    public void initialize(Connection connection) throws SQLException {
        try {
            MySqlHelper.runSqlFile(connection, "drop_mysql.sql");
            MySqlHelper.runSqlFile(connection, "create_mysql.sql");
            MySqlHelper.insertTestData(connection, 10_000);
        } catch (RuntimeSqlException e) {
            MySqlHelper.handleSqlException(e);
        }
    }

    @Override
    public void close() throws SQLException {
    }

}
