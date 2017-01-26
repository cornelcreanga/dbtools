package com.ccreanga.integration.mysql;

import com.ccreanga.Config;
import com.ccreanga.DataSource;
import com.ccreanga.jdbc.Dialect;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.usecases.export.jdbc.SqlTablesExport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestExportMysql {

    private String user;
    private String password;
    private String schema;
    private String server;
    private Connection connection;
    private MySqlDbSetup setup = new MySqlDbSetup();

    @Before
    public void staticSetup() throws SQLException {
        DataSource dataSource = Config.getConfig().getDataSource(Dialect.MYSQL);
        user = dataSource.getUser();
        password = dataSource.getPassword();
        if (password == null)
            password = "";
        schema = dataSource.getSchema();
        server = dataSource.getServer();
        try {
            connection = DriverManager.getConnection(server + "/" + schema + "?user=" + user + "&password=" + password + "&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true");
        } catch (SQLException e) {
            System.out.printf("cannot open a connection to mysql using user=%s,password=%s,server=%s,schema=%s. " +
                    "in order to run the tests you need a mysql server properly configured (check application.yml file)", user, password, server, schema);
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
        sqlTablesExport.exportTables(new DbConnection(connection, Dialect.MYSQL), new Schema(schema), "*", "/tmp", true);
        //todo run an mysql export; compare files
        /**
         SELECT a,b,a+b INTO OUTFILE '/tmp/result.txt'
         FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
         LINES TERMINATED BY '\n'
         FROM test_table;
         */
        //or import both exported files and check if the tables are identical

    }

}
