package com.ccreanga.anonymizer;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class TimestampAnonymizer implements Anonymizer {

    private int secondsNegDeviation = 1000;
    private int secondsPosDeviation = 1000;

    @Override
    public Object anonymize(Object original, List<Object> fullRow) {
        Random r = new Random();
        int days = r.nextInt(secondsNegDeviation + secondsPosDeviation);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime((Timestamp) original);
        calendar.add(Calendar.SECOND, days - secondsNegDeviation);
        return new Timestamp(calendar.getTimeInMillis());
    }

    public void setSecondsNegDeviation(int secondsNegDeviation) {
        this.secondsNegDeviation = secondsNegDeviation;
    }

    public void setSecondsPosDeviation(int secondsPosDeviation) {
        this.secondsPosDeviation = secondsPosDeviation;
    }
}
