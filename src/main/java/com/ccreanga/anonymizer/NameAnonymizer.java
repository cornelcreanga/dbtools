package com.ccreanga.anonymizer;


import com.ccreanga.random.RandomNameGenerator;

import java.io.IOException;
import java.net.URL;

public class NameAnonymizer {

    private static RandomNameGenerator generator;

    static{
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource("fantasy.txt");
            if (url==null)
                throw new RuntimeException("can't locate the file fantasy.txt in the classpath");
            generator = new RandomNameGenerator(url.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int sylNumber;
    private boolean rememberValues;

    public String anonymize(String original){
        return generator.compose(sylNumber);
    }

    public NameAnonymizer() {
        this(2,false);
    }

    public NameAnonymizer(int sylNumber, boolean rememberValues) {
        this.sylNumber = sylNumber;
        this.rememberValues = rememberValues;
    }

    public void setSylNumber(int sylNumber) {
        this.sylNumber = sylNumber;
    }

    public void setRememberValues(boolean rememberValues) {
        this.rememberValues = rememberValues;
    }
}
