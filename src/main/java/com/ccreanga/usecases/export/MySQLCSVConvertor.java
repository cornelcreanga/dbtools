package com.ccreanga.usecases.export;

import org.apache.commons.codec.binary.Hex;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class MySQLCSVConvertor {

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");


    public String toStringConvertor(Object value) {
        if (value == null)
            return null;
        if (value instanceof String)
            return (String) value;
        if (value instanceof BigDecimal) {
            return ((BigDecimal)value).toPlainString();
        }
        if ((value instanceof Float) || (value instanceof Double)) {
            return value.toString();
        }
        if (value instanceof Number) {
            return value.toString();
        }
        if (value instanceof java.sql.Date) {
            return dateFormatter.format(((java.sql.Date)value).toLocalDate());
        }
        if (value instanceof java.sql.Time) {
            return timeFormatter.format(((java.sql.Time)value).toLocalTime());
        }

        if (value instanceof java.sql.Timestamp) {
            return dateTimeFormatter.format(((java.sql.Timestamp)value).toLocalDateTime());
        }

        if (value instanceof Boolean) {
            Boolean b = (Boolean)value;
            return b ?"1":"0";
        }

        if (value instanceof byte[]) {
            return Hex.encodeHexString((byte[])value);
        }
        throw new RuntimeException("cannot convert value;unknown type:" + value.getClass());
    }


}
