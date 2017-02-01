package com.ccreanga.usecases.export.jdbc.mysql;

import com.ccreanga.jdbc.DatabaseException;
import com.ccreanga.util.Encoding;
import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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

        if (value instanceof InputStream) {
            try {
                return Encoding.encodeHexString(ByteStreams.toByteArray((InputStream)value));
            } catch (IOException e) {
                throw new DatabaseException(e);
            }
        }

        if (value instanceof Reader) {
            try{
                return CharStreams.toString((Reader)value);
            } catch (IOException e) {
                throw new DatabaseException(e);
            }
        }
        throw new RuntimeException("cannot convert value;unknown type:" + value.getClass());
    }


}
