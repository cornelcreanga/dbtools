package com.ccreanga.anonymizer;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class DateAnonymizer implements Anonymizer {

    private int daysNegDeviation = 1000;
    private int daysPosDeviation = 1000;

    public DateAnonymizer() {
    }

    @Override
    public Object anonymize(Object original,List<Object> fullRow) {
        Random r = new Random();
        int days = r.nextInt(daysNegDeviation + daysPosDeviation);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime((Date) original);
        calendar.add(Calendar.DAY_OF_YEAR, days - daysNegDeviation);
        return new Date(calendar.getTimeInMillis());
    }

    public void setDaysNegDeviation(int daysNegDeviation) {
        this.daysNegDeviation = daysNegDeviation;
    }

    public void setDaysPosDeviation(int daysPosDeviation) {
        this.daysPosDeviation = daysPosDeviation;
    }
}
