package com.ccreanga.anonymizer;

import org.apache.commons.lang3.RandomStringUtils;

public class StringAnonymizer {

    public String anonymize(String original, int count){
        return RandomStringUtils.randomAlphabetic(count);
    }

}
