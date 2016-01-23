package com.ccreanga.anonymizer;

import java.util.Random;

public class DoubleAnonymizer {

    public Double anonymize(Number original,double rangeMin, double rangeMax){
        Random r = new Random();
        return rangeMin + (rangeMax - rangeMin) * r.nextDouble();
    }

}
