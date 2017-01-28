package com.ccreanga.integration.oracle;

import com.ccreanga.Config;
import com.ccreanga.DataSource;
import com.ccreanga.integration.mysql.MySqlDbSetup;
import com.ccreanga.jdbc.Dialect;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.usecases.export.jdbc.SqlTablesExport;
import oracle.jdbc.pool.OracleDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestExportOrcl {

    private String user;
    private String password;
    private String schema;
    private String server;
    private Connection connection;
    private OracleDbSetup setup = new OracleDbSetup();

    @Before
    public void staticSetup() throws SQLException {
        DataSource dataSource = Config.getConfig().getDataSource(Dialect.ORACLE);
        user = dataSource.getUser();
        password = dataSource.getPassword();
        if (password == null)
            password = "";
        schema = dataSource.getSchema();
        server = dataSource.getServer();
        try {
            OracleDataSource ods = new OracleDataSource();
            ods.setURL(server);
            ods.setUser(user);
            ods.setPassword(password);
            connection = ods.getConnection();
        } catch (SQLException e) {
            System.out.printf("cannot open a connection to orcl using user=%s,password=%s,server=%s. " +
                    "in order to run the tests you need a orcl server properly configured (check application.yml file)", user, password, server, schema);
            System.exit(-1);
        }
        connection.setAutoCommit(false);
        setup.initialize(connection);
    }

    @After
    public void staticTearDown() throws Exception {
        setup.close();
    }

    @Test
    public void testExport() {
        //DataAnonymizer anonymizer = new DataAnonymizer(Thread.currentThread().getContextClassLoader().getResource("anonymizer1.yml").getPath());
        //SqlTablesExport sqlTablesExport = new SqlTablesExport(anonymizer);
        SqlTablesExport sqlTablesExport = new SqlTablesExport();
        sqlTablesExport.exportTables(new DbConnection(connection, Dialect.ORACLE), new Schema(schema), "*", "/tmp", true);


    }

}
