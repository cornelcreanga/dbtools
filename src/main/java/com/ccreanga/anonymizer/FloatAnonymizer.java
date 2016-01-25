package com.ccreanga.anonymizer;

import java.util.Random;

public class FloatAnonymizer implements Anonymizer{

    private double rangeMin = Float.MIN_VALUE;
    private double rangeMax = Float.MAX_VALUE;

    public void setRangeMin(float rangeMin) {
        this.rangeMin = rangeMin;
    }

    public void setRangeMax(float rangeMax) {
        this.rangeMax = rangeMax;
    }

    @Override
    public Object anonymize(Object original) {
        Random r = new Random();
        return (float)(rangeMin + (rangeMax - rangeMin) * r.nextDouble());//todo - prevent overflow

    }
}
