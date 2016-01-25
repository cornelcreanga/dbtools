package com.ccreanga.anonymizer;

import java.util.Random;

public class LongAnonymizer implements Anonymizer{

    private long rangeMin;
    private long rangeMax;

    public LongAnonymizer() {
        this(Integer.MIN_VALUE,Integer.MAX_VALUE);
    }

    public LongAnonymizer(long rangeMin, long rangeMax) {
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    public void setRangeMin(long rangeMin) {
        this.rangeMin = rangeMin;
    }

    public void setRangeMax(long rangeMax) {
        this.rangeMax = rangeMax;
    }

    @Override
    public Object anonymize(Object original) {
        Random r = new Random();
        return (long)(rangeMin + (rangeMax - rangeMin) * r.nextDouble());//todo - prevent overflow

    }
}
