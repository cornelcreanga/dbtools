package com.ccreanga.integration.oracle;

import com.ccreanga.DbSetup;
import com.ccreanga.OracleHelper;
import com.ccreanga.jdbc.RuntimeSqlException;

import java.sql.Connection;
import java.sql.SQLException;

public class OracleDbSetup implements DbSetup {

    @Override
    public void initialize(Connection connection) throws SQLException {
        try {
            OracleHelper.runSqlFile(connection, "drop_orcl.sql");
            OracleHelper.runSqlFile(connection, "create_orcl.sql");
            OracleHelper.insertTestData(connection, 2);
        } catch (RuntimeSqlException e) {
            OracleHelper.handleSqlException(e);
        }
    }

    @Override
    public void close() throws SQLException {
    }

}
