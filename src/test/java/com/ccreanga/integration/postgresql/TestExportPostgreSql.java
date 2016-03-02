package com.ccreanga.integration.postgresql;

import com.ccreanga.Config;
import com.ccreanga.DBToolsApplication;
import com.ccreanga.DataSource;
import com.ccreanga.jdbc.Dialect;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.usecases.export.SqlTablesExport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestExportPostgreSql {

    private String user;
    private String password;
    private String schema;
    private String server;
    private Connection connection;
    private PostgreSqlDbSetup setup = new PostgreSqlDbSetup();

    @Before
    public void staticSetup() throws Exception {
        Class.forName("org.postgresql.Driver");
        DataSource dataSource = Config.getConfig().getDataSource(Dialect.POSTGRESQL);
        user = dataSource.getUser();
        password = dataSource.getPassword();
        schema = dataSource.getSchema();
        server = dataSource.getServer();

        connection = DriverManager.getConnection(server+"/"+schema+"?user="+user+"&password="+password);
        connection.setAutoCommit(false);
        setup.initialize(connection);
    }
    @After
    public void staticTearDown() throws Exception {
        setup.close();
    }

    @Test
    public void testExport(){
        SqlTablesExport sqlTablesExport = new SqlTablesExport();
        sqlTablesExport.exportTables(new DbConnection(connection,Dialect.POSTGRESQL), new Schema(schema), "*", "/tmp", true);

    }

}
