package com.ccreanga.integration.mysql;

import com.ccreanga.Config;
import com.ccreanga.DBToolsApplication;
import com.ccreanga.DataSource;
import com.ccreanga.jdbc.Dialect;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.usecases.export.DataAnonymizer;
import com.ccreanga.usecases.export.SqlTablesExport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    public void staticSetup() throws SQLException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("cannot find mysql jdbc driver. this should never happen unless something happened with the maven repo ");
            System.exit(-1);
        }
        DataSource dataSource = Config.getConfig().getDataSource(Dialect.MYSQL);
        user = dataSource.getUser();
        password = dataSource.getPassword();
        if (password==null)
            password = "";
        schema = dataSource.getSchema();
        server = dataSource.getServer();
        try {
            connection = DriverManager.getConnection(server+"/"+schema+"?user="+user+"&password="+password+"&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true");
        } catch (SQLException e) {
            System.out.printf("cannot open a connection to mysql using user=%s,password=%s,server=%s,schema=%s. " +
                    "in order to run the tests you need a mysql server properly configured (check application.yml file)",user,password,server,schema);
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
    public void testExport(){
        //DataAnonymizer anonymizer = new DataAnonymizer(Thread.currentThread().getContextClassLoader().getResource("anonymizer1.yml").getPath());
        //SqlTablesExport sqlTablesExport = new SqlTablesExport(anonymizer);
        SqlTablesExport sqlTablesExport = new SqlTablesExport();
        sqlTablesExport.exportTables(new DbConnection(connection,Dialect.MYSQL), new Schema(schema), "*", "/tmp", true);
    }

}
