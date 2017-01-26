package com.ccreanga.integration.oracle;

import com.ccreanga.DbSetup;
import com.ccreanga.TestHelper;
import com.ccreanga.jdbc.RuntimeSqlException;

import java.sql.Connection;
import java.sql.SQLException;

public class OracleDbSetup implements DbSetup {

    @Override
    public void initialize(Connection connection) throws SQLException {
        try {
            TestHelper.runSqlFile(connection, "drop_orcl.sql");
            TestHelper.runSqlFile(connection, "create_orcl.sql");
            TestHelper.insertTestData(connection, 10_000);
        } catch (RuntimeSqlException e) {
            TestHelper.handleSqlException(e);
        }
    }

    @Override
    public void close() throws SQLException {
    }

}
