package com.ccreanga.integration.mysql;

import com.ccreanga.DbSetup;
import com.ccreanga.TestHelper;
import com.ccreanga.jdbc.RuntimeSqlException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDbSetup implements DbSetup {

    @Override
    public void initialize(Connection connection) throws SQLException {
        try {
            TestHelper.runSqlFile(connection,"drop_mysql.sql");
            TestHelper.runSqlFile(connection,"create_mysql.sql");
            TestHelper.insertTestData(connection, 10_000);
        }catch (RuntimeSqlException e){
            TestHelper.handleSqlException(e);
        }
    }

    @Override
    public void close() throws SQLException {
    }

}
