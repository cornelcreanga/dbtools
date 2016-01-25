package com.ccreanga.anonymizer;

import java.util.Random;

public class IntegerAnonymizer implements Anonymizer{

    private int rangeMin;
    private int rangeMax;

    public IntegerAnonymizer() {
        this(Integer.MIN_VALUE,Integer.MAX_VALUE);
    }

    public IntegerAnonymizer(int rangeMin, int rangeMax) {
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

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