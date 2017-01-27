package com.ccreanga.integration.postgresql;

import com.ccreanga.DbSetup;
import com.ccreanga.MySqlHelper;
import com.ccreanga.PostgreSqlHelper;
import com.ccreanga.jdbc.Dialect;
import com.ccreanga.jdbc.RuntimeSqlException;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgreSqlDbSetup implements DbSetup {

    private Connection connection;

    @Override
    public void initialize(Connection connection) throws SQLException {

        try {
            PostgreSqlHelper.runSqlFile(connection, "drop_postgresql.sql");
            PostgreSqlHelper.runSqlFile(connection, "create_postgresql.sql");
            PostgreSqlHelper.insertTestData(connection, 10_000);
        } catch (RuntimeSqlException e) {
            MySqlHelper.handleSqlException(e);
        }
    }

    @Override
    public void close() throws SQLException {
    }

    public Connection getConnection() {
        return connection;
    }
}
