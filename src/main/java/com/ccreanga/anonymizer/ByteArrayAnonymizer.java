package com.ccreanga.anonymizer;

import java.util.Random;

public class ByteArrayAnonymizer {

    public byte[] anonymize(byte[] original,int size){
        Random r = new Random();
        byte[] random = new byte[size];
        r.nextBytes(random);
        return random;
    }

}
