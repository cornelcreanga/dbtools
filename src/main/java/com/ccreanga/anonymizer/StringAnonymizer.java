package com.ccreanga.anonymizer;


import com.ccreanga.RandomStringUtils;

public class StringAnonymizer implements Anonymizer {

    private int count = -1;

    @Override
    public Object anonymize(Object original) {
        int len = count == -1 ? ((String) original).length() : count;
        return RandomStringUtils.randomAlphabetic(len);
    }

    public void setCount(int count) {
        this.count = count;
    }
}
