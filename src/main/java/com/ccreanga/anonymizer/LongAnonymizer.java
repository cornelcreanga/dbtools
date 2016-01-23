package com.ccreanga.anonymizer;

import java.util.Random;

public class LongAnonymizer {

    private long rangeMin;
    private long rangeMax;

    public LongAnonymizer() {
        this(Integer.MIN_VALUE,Integer.MAX_VALUE);
    }

    public LongAnonymizer(long rangeMin, long rangeMax) {
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    public Long anonymize(Number original){
        Random r = new Random();
        return (long)(rangeMin + (rangeMax - rangeMin) * r.nextDouble());
    }


    public void setRangeMin(long rangeMin) {
        this.rangeMin = rangeMin;
    }

    public void setRangeMax(long rangeMax) {
        this.rangeMax = rangeMax;
    }
}
