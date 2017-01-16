package com.ccreanga.anonymizer;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class TimeAnonymizer implements Anonymizer {

    private int secondsNegDeviation = 1000;
    private int secondsPosDeviation = 1000;

    @Override
    public Object anonymize(Object original) {
        Random r = new Random();
        int days = r.nextInt(secondsNegDeviation + secondsPosDeviation);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime((Time) original);
        calendar.add(Calendar.SECOND, days - secondsNegDeviation);
        return new Time(calendar.getTimeInMillis());
    }

    public void setSecondsNegDeviation(int secondsNegDeviation) {
        this.secondsNegDeviation = secondsNegDeviation;
    }

    public void setSecondsPosDeviation(int secondsPosDeviation) {
        this.secondsPosDeviation = secondsPosDeviation;
    }
}