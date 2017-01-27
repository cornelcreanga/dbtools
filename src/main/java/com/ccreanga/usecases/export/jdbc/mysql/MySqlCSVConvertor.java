package com.ccreanga.usecases.export.jdbc.mysql;

import com.ccreanga.util.Encoding;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class MySqlCSVConvertor {

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeStampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");


    public String toStringConvertor(Object value) {
        if (value == null)
            return null;
        if (value instanceof String)
            return (String) value;
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).toPlainString();
        }
        if ((value instanceof Float) || (value instanceof Double)) {
            return value.toString();
        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof Date) {
            return dateFormatter.format(((Date) value).toLocalDate());
        }
        if (value instanceof Time) {
            return timeFormatter.format(((Time) value).toLocalTime());
        }

        if (value instanceof Timestamp) {
            return timeStampFormatter.format(((Timestamp) value).toLocalDateTime());
        }

        if (value instanceof Boolean) {
            Boolean b = (Boolean) value;
            return b ? "1" : "0";
        }

        if (value instanceof byte[]) {
            return Encoding.encodeHexString((byte[]) value);
        }
        throw new RuntimeException("cannot convert value;unknown type:" + value.getClass());
    }


}