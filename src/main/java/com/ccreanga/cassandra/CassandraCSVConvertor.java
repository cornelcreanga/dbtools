package com.ccreanga.cassandra;

import com.ccreanga.util.Encoding;
import com.datastax.driver.core.LocalDate;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CassandraCSVConvertor {

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeStampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");

    //00:10:47.446164472,1986-04-22 14:09:24+0000
    public String toStringConvertor(Object value) {
        if (value == null)
            return null;
        if (value instanceof String)
            return (String) value;
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toPlainString();
        }
//        if ((value instanceof Float) || (value instanceof Double)) {
//            return value.toString();
//        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof LocalDate) {
            LocalDate v = (LocalDate)value;
            return dateFormatter.format(java.time.LocalDate.of(v.getYear(),v.getMonth(),v.getDay()));
        }
        if (value instanceof Time) {
            return timeFormatter.format(((Time) value).toLocalTime());
        }
        if (value instanceof Date) {
            Date v = (Date)value;
            Instant instant = Instant.ofEpochMilli(v.getTime());
            LocalDateTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            return dateFormatter.format(res);
        }


        if (value instanceof Boolean) {
            Boolean b = (Boolean) value;
            return b ? "True" : "False";
        }

        if (value instanceof UUID){
            return (value.toString());
        }

        if (value instanceof InetAddress){
            return value.toString();
        }
        if (value instanceof List){
            return "";
        }
        if (value instanceof Set){
            return "";
        }
        if (value instanceof Map){
            return "";
        }


        if (value instanceof byte[]) {
            return "0x"+Encoding.encodeHexString((byte[]) value);
        }
        throw new RuntimeException("cannot convert value;unknown type:" + value.getClass());
    }

}
