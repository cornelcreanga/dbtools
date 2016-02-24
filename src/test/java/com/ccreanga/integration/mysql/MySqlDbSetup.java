package com.ccreanga.integration.mysql;

import com.ccreanga.DbSetup;
import com.ccreanga.TestHelper;
import com.ccreanga.jdbc.RuntimeSqlException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDbSetup implements DbSetup {

    private Connection connection;

    @Override
    public void initialize(String server,String schema,String user,String password) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(server+"/"+schema+"?user="+user+"&password="+password+"&zeroDateTimeBehavior=convertToNull");
        connection.setAutoCommit(false);

        try {
            TestHelper.runSqlFile(connection,"drop_mysql.sql");
            TestHelper.runSqlFile(connection,"create_mysql.sql");
            TestHelper.insertTestData(connection, 100_000);
        }catch (RuntimeSqlException e){
            TestHelper.handleSqlException(e);
        }

    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    public Connection getConnection() {
        return connection;
    }
}
