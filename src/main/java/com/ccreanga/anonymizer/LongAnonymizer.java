package com.ccreanga.anonymizer;

import java.util.Random;

public class LongAnonymizer implements Anonymizer {

    private long rangeMin = Long.MIN_VALUE;
    private long rangeMax = Long.MAX_VALUE;

    public void setRangeMin(long rangeMin) {
        this.rangeMin = rangeMin;
    }

    public void setRangeMax(long rangeMax) {
        this.rangeMax = rangeMax;
    }

    @Override
    public Object anonymize(Object original) {
        Random r = new Random();
        return (long) (rangeMin + (rangeMax - rangeMin) * r.nextDouble());//todo - prevent overflow

    }
}
