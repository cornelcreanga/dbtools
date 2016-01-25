package com.ccreanga.anonymizer;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class TimeAnonymizer implements Anonymizer{

    private int secondsNegDeviation = 1000;
    private int secondsPosDeviation = 1000;

    public TimeAnonymizer() {
    }

    public TimeAnonymizer(int secondsNegDeviation, int secondsPosDeviation) {
        this.secondsNegDeviation = secondsNegDeviation;
        this.secondsPosDeviation = secondsPosDeviation;
    }

    @Override
    public Object anonymize(Object original) {
        Random r = new Random();
        int days = r.nextInt(secondsNegDeviation+secondsPosDeviation);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime((Date)original);
        calendar.add(Calendar.SECOND,days-secondsNegDeviation);
        return new Date(calendar.getTimeInMillis());
    }
}