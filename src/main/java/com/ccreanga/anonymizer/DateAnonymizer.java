package com.ccreanga.anonymizer;

import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class DateAnonymizer implements Anonymizer{

    private int daysNegDeviation = 1000;
    private int daysPosDeviation = 1000;

    public DateAnonymizer() {
    }

    public DateAnonymizer(int daysNegDeviation, int daysPosDeviation) {
        this.daysNegDeviation = daysNegDeviation;
        this.daysPosDeviation = daysPosDeviation;
    }

    @Override
    public Object anonymize(Object original) {
        Random r = new Random();
        int days = r.nextInt(daysNegDeviation+daysPosDeviation);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime((Date)original);
        calendar.add(Calendar.DAY_OF_YEAR,days-daysNegDeviation);
        return new Date(calendar.getTimeInMillis());
    }
}
