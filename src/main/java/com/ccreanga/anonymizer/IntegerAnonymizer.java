package com.ccreanga.anonymizer;

import java.util.Random;

public class IntegerAnonymizer implements Anonymizer{

    private int rangeMin=Integer.MIN_VALUE;
    private int rangeMax=Integer.MAX_VALUE;

    public void setRangeMin(int rangeMin) {
        this.rangeMin = rangeMin;
    }

    public void setRangeMax(int rangeMax) {
        this.rangeMax = rangeMax;
    }

    @Override
    public Object anonymize(Object original) {
        Random r = new Random();
        return (long)(rangeMin + (rangeMax - rangeMin) * r.nextDouble());

    }
}