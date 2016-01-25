package com.ccreanga.usecases.export;

import com.ccreanga.DbtoolsApplication;
import com.ccreanga.TestHelper;
import com.ccreanga.anonymizer.Anonymizer;
import com.ccreanga.anonymizer.NameAnonymizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.sql.DriverManager;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DbtoolsApplication.class)
public class DataAnonymizerTest {

    DataAnonymizer dataAnonymizer;

    @Before
    public void staticSetup() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("anonymizer1.yml");
        if (url==null)
            throw new RuntimeException("can't locate the file anonymizer1.yml in the classpath");
        dataAnonymizer = new DataAnonymizer(url.getFile());
    }


    @Test
    public void testShouldAnonymize() throws Exception {
        Assert.assertEquals(dataAnonymizer.shouldAnonymize(), true);
    }

    @Test
    public void testGetAnonymizer() throws Exception {
        Optional<Anonymizer> anonymizer = dataAnonymizer.getAnonymizer("test_types","c_varchar");
        Assert.assertEquals(anonymizer.isPresent(),true);
        Assert.assertEquals(anonymizer.get() instanceof NameAnonymizer,true);
        NameAnonymizer nameAnonymizer = (NameAnonymizer) anonymizer.get();
        Assert.assertEquals(nameAnonymizer.getSylNumber(),3);
        Assert.assertEquals(nameAnonymizer.isRememberValues(),true);

        //anonymizer.get()

    }
}