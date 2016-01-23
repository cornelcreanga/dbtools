package com.ccreanga.anonymizer;

import java.util.Calendar;
import java.sql.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class DateAnonymizer {

    public Date anonymize(Date original, int daysNegDeviation, int daysPosDeviation){
        Random r = new Random();
        int days = r.nextInt(daysNegDeviation+daysPosDeviation);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(original);
        calendar.add(Calendar.DAY_OF_YEAR,days-daysNegDeviation);
        return new Date(calendar.getTimeInMillis());
    }

}
