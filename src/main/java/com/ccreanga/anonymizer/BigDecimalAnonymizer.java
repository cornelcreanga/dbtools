package com.ccreanga.anonymizer;

import java.math.BigDecimal;
import java.util.List;

public class BigDecimalAnonymizer implements Anonymizer {

    private BigDecimal rangeMin;
    private BigDecimal rangeMax;

    public void setRangeMin(BigDecimal rangeMin) {
        this.rangeMin = rangeMin;
    }

    public void setRangeMax(BigDecimal rangeMax) {
        this.rangeMax = rangeMax;
    }

    @Override
    public Object anonymize(Object original, List<Object> fullRow) {
        return rangeMin.add(new BigDecimal(Math.random()).multiply(rangeMax.subtract(rangeMin)));
    }
}
