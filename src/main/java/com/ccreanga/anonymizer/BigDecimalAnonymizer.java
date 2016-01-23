package com.ccreanga.anonymizer;

import java.math.BigDecimal;
import java.util.Random;

public class BigDecimalAnonymizer {

    public BigDecimal anonymize(Number original, double rangeMin, double rangeMax){
        Random r = new Random();
        return new BigDecimal(rangeMin + (rangeMax - rangeMin) * r.nextDouble());
    }

}
