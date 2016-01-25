package com.ccreanga.anonymizer;


import com.ccreanga.random.RandomNameGenerator;

import java.io.IOException;
import java.net.URL;

public class NameAnonymizer implements Anonymizer{

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

    private int sylNumber = 2;
    private int wordNumber = 1;
    private boolean rememberValues;

    public Object anonymize(Object original){
        if (wordNumber==1)//optimization
            return generator.compose(sylNumber);
        StringBuilder sb = new StringBuilder();
        for(int i= 0;i<wordNumber;i++){
            sb.append(generator.compose(sylNumber));
            if (i!=(wordNumber-1))
                sb.append(" ");
        }
        return sb.toString();
    }

    public NameAnonymizer() {
    }

    public void setSylNumber(int sylNumber) {
        this.sylNumber = sylNumber;
    }

    public void setRememberValues(boolean rememberValues) {
        this.rememberValues = rememberValues;
    }

    public void setWordNumber(int wordNumber) {
        this.wordNumber = wordNumber;
    }

    public int getSylNumber() {
        return sylNumber;
    }

    public int getWordNumber() {
        return wordNumber;
    }

    public boolean isRememberValues() {
        return rememberValues;
    }
}
