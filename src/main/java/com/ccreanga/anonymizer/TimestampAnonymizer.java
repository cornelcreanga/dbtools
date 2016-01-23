package com.ccreanga.anonymizer;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class TimestampAnonymizer {

    public Timestamp anonymize(Timestamp original, int secondsNegDeviation, int secondsPosDeviation){
        Random r = new Random();
        int days = r.nextInt(secondsNegDeviation+secondsPosDeviation);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(original);
        calendar.add(Calendar.DAY_OF_YEAR,days-secondsNegDeviation);
        return new Timestamp(calendar.getTimeInMillis());
    }


}
