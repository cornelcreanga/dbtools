package com.ccreanga.integration.postgresql;

import com.ccreanga.DbSetup;
import com.ccreanga.TestHelper;
import com.ccreanga.jdbc.RuntimeSqlException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSqlDbSetup implements DbSetup {

    private Connection connection;

    @Override
    public void initialize(Connection connection) throws Exception {

        try {
            TestHelper.runSqlFile(connection,"drop_postgresql.sql");
            TestHelper.runSqlFile(connection,"create_postgresql.sql");
            TestHelper.insertTestData(connection, 10_000);
        }catch (RuntimeSqlException e){
            TestHelper.handleSqlException(e);        }
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    public Connection getConnection() {
        return connection;
    }
}
