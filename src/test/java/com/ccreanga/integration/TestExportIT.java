package com.ccreanga.integration;

import com.ccreanga.DBToolsApplication;
import com.ccreanga.TestHelper;
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
@ConfigurationProperties(prefix="testDatasource")
public class TestExportIT {

    private String user;
    private String passwdord;
    private String schema;
    private String server;
    private String db;
    private Connection connection;


    @Before
    public void staticSetup() throws Exception {

        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection(server+"/"+schema+"?user="+user+"&password="+passwdord+"&zeroDateTimeBehavior=convertToNull");
        connection.setAutoCommit(false);

        TestHelper.dropTables(connection);
        TestHelper.createTables(connection);
        TestHelper.insertTestData(connection,1_000_000);

    }

    @After
    public void staticTearDown() throws Exception {
        connection.close();
    }

    @Test
    public void testExport(){

    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPasswdord(String passwdord) {
        this.passwdord = passwdord;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setDb(String db) {
        this.db = db;
    }
}
