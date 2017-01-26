package com.ccreanga.usecases.export;

import com.ccreanga.anonymizer.Anonymizer;
import com.ccreanga.anonymizer.NameAnonymizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Optional;

public class DataAnonymizerTest {

    DataAnonymizer dataAnonymizer;

    @Before
    public void staticSetup() throws Exception {
        URL url = Thread.currentThread().getContextClassLoader().getResource("anonymizer1.yml");
        if (url == null)
            throw new RuntimeException("cannot load anonymizer1.yml");
        dataAnonymizer = new DataAnonymizer(url.getFile());
    }


    @Test
    public void testShouldAnonymize() throws Exception {
        Assert.assertEquals(dataAnonymizer.shouldAnonymize(), true);
    }

    @Test
    public void testGetAnonymizer() throws Exception {
        Optional<Anonymizer> anonymizer = dataAnonymizer.getAnonymizer("test_types", "c_varchar");
        Assert.assertEquals(anonymizer.isPresent(), true);
        Assert.assertEquals(anonymizer.get() instanceof NameAnonymizer, true);
        NameAnonymizer nameAnonymizer = (NameAnonymizer) anonymizer.get();
        Assert.assertEquals(nameAnonymizer.getSylNumber(), 3);
        Assert.assertEquals(nameAnonymizer.getStore(), "test_types_c_varchar_store");

        //anonymizer.get()

    }
}