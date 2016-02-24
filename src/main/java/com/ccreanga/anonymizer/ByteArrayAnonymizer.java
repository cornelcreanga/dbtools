package com.ccreanga.anonymizer;

import java.util.Random;

public class ByteArrayAnonymizer implements Anonymizer{

    private int count=-1;

    @Override
    public Object anonymize(Object original) {
        Random r = new Random();
        byte[] originalBytes = (byte[])original;
        int len = count==-1?originalBytes.length:count;
        byte[] random = new byte[len];
        r.nextBytes(random);
        return random;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
