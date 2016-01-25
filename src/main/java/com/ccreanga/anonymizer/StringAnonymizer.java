package com.ccreanga.anonymizer;

import org.apache.commons.lang3.RandomStringUtils;

public class StringAnonymizer implements Anonymizer{

    private int count;

    @Override
    public Object anonymize(Object original) {
        return RandomStringUtils.randomAlphabetic(count);
    }

    public void setCount(int count) {
        this.count = count;
    }
}
