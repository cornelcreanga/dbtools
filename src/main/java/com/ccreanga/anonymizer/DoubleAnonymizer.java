package com.ccreanga.anonymizer;

import java.util.Random;

public class DoubleAnonymizer implements Anonymizer{

    private double rangeMin;
    private double rangeMax;

    public DoubleAnonymizer() {
        this(Double.MIN_VALUE,Double.MAX_VALUE);
    }

    public DoubleAnonymizer(double rangeMin, double rangeMax) {
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
    }

    public void setRangeMin(double rangeMin) {
        this.rangeMin = rangeMin;
    }

    public void setRangeMax(double rangeMax) {
        this.rangeMax = rangeMax;
    }

    @Override
    public Object anonymize(Object original) {
        Random r = new Random();
        return (double)(rangeMin + (rangeMax - rangeMin) * r.nextDouble());//todo - prevent overflow

    }
}
