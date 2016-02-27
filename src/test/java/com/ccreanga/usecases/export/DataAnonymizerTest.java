package com.ccreanga.usecases.export;

import com.ccreanga.DBToolsApplication;
import com.ccreanga.anonymizer.Anonymizer;
import com.ccreanga.anonymizer.NameAnonymizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DBToolsApplication.class)
public class DataAnonymizerTest {

    DataAnonymizer dataAnonymizer;

    @Before
    public void staticSetup() throws Exception {
        dataAnonymizer = new DataAnonymizer("anonymizer1.yml");
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