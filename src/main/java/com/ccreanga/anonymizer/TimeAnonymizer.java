package com.ccreanga.anonymizer;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

public class TimeAnonymizer {

    public Time anonymize(Time original, int secondsNegDeviation, int secondsPosDeviation){
        Random r = new Random();
        int seconds = r.nextInt(secondsNegDeviation+secondsPosDeviation);
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(original);
        calendar.add(Calendar.DAY_OF_YEAR,seconds-secondsNegDeviation);
        return new Time(calendar.getTimeInMillis());
    }

}
