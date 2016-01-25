package com.ccreanga.anonymizer;

import java.util.Random;

public class ByteArrayAnonymizer implements Anonymizer{

    private int count;

    @Override
    public Object anonymize(Object original) {
        Random r = new Random();
        byte[] random = new byte[count];
        r.nextBytes(random);
        return random;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
