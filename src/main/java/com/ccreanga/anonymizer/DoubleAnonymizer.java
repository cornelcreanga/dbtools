package com.ccreanga.anonymizer;

import java.util.List;
import java.util.Random;

public class DoubleAnonymizer implements Anonymizer {

    private double rangeMin = Double.MIN_VALUE;
    private double rangeMax = Double.MAX_VALUE;

    public void setRangeMin(double rangeMin) {
        this.rangeMin = rangeMin;
    }

    public void setRangeMax(double rangeMax) {
        this.rangeMax = rangeMax;
    }

    @Override
    public Object anonymize(Object original,List<Object> fullRow) {
        Random r = new Random();
        return (rangeMin + (rangeMax - rangeMin) * r.nextDouble());//todo - prevent overflow

    }
}
