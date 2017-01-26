package com.ccreanga.anonymizer;


import com.ccreanga.RandomStringUtils;

import java.util.List;

public class StringAnonymizer implements Anonymizer {

    private int count = -1;

    @Override
    public Object anonymize(Object original, List<Object> fullRow) {
        int len = count == -1 ? ((String) original).length() : count;
        return RandomStringUtils.randomAlphabetic(len);
    }

    public String anonymize(int count) {
        return RandomStringUtils.randomAlphabetic(count);
    }

    public void setCount(int count) {
        this.count = count;
    }
}
