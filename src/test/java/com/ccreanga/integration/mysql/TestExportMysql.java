package com.ccreanga.integration.mysql;

import com.ccreanga.DBToolsApplication;
import com.ccreanga.jdbc.Dialect;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.usecases.export.SqlTablesExport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Connection;
import java.sql.DriverManager;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DBToolsApplication.class)
@IntegrationTest
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="mySqlDatasource")
public class TestExportMysql {

    private String user;
    private String password;
    private String schema;
    private String server;
    private Connection connection;
    private MySqlDbSetup setup = new MySqlDbSetup();

    @Before
    public void staticSetup() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(server+"/"+schema+"?user="+user+"&password="+password+"&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true");
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
        sqlTablesExport.exportTables(new DbConnection(connection,Dialect.MYSQL), new Schema(schema), "*", "/tmp", true);
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setServer(String server) {
        this.server = server;
    }

}
