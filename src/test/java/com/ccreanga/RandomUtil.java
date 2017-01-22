package com.ccreanga;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class RandomUtil {


    public static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    public static byte[] generateBytes(int len) {
        byte[] data = new byte[len];
        new Random().nextBytes(data);
        return data;
    }

    public static String generateString(int len) {
        Random random = new Random();
        char[] chars = "abcdefghijklmnopqrstuvwxyz\n".toCharArray();
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static long generateDate() {
        Calendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        int year = randBetween(1975, 2010);
        gc.set(Calendar.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));
        gc.set(Calendar.DAY_OF_YEAR, dayOfYear);
        return gc.getTime().getTime();
    }
}
