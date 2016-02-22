package com.ccreanga.integration.postgresql;

import com.ccreanga.DBToolsApplication;
import com.ccreanga.integration.mysql.MySqlDbSetup;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DBToolsApplication.class)
@IntegrationTest
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="postgreSqlDatasource")
public class TestExportPostgreSql {

    private String user;
    private String password;
    private String schema;
    private String server;
    private PostgreSqlDbSetup setup = new PostgreSqlDbSetup();

    @Before
    public void staticSetup() throws Exception {
        setup.initialize(server,schema,user,password);
    }
    @After
    public void staticTearDown() throws Exception {
        setup.close();
    }


    @Test
    public void testExport(){

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
