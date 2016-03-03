package com.ccreanga.usecases.export.postgresql;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class PostgreSqlCSVConvertor {

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    DateTimeFormatter timeStampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");


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
            return bytea((byte[]) value);
        }
        throw new RuntimeException("cannot convert value;unknown type:" + value.getClass());
    }

    /**
     * PostgreSql bytea conversion implementation
     * Rules are
     * 92	backslash	\\
     * 0 to 31 and 127 to 255	"non-printable" octets	\xxx (octal value)
     * 32 to 126	"printable" octets	client character set representation
     * @param input
     * @return
     */
    public String bytea(byte[] input) {
        StringBuilder sb = new StringBuilder(input.length + 32);
        for (byte anInput : input) {
            int unsigned = anInput & 0xFF;
            if (unsigned == 92)
                sb.append("\\\\");
            else if (((unsigned >= 0) && (unsigned <= 31)) || ((unsigned >= 127)))
                sb.append("").append(toOctal(unsigned));
            else
                sb.append((char) unsigned);

        }
        return sb.toString();


    }

    public String toOctal(int b) {
        String octal = Integer.toString(b, 8);
        if (octal.length() == 1)
            return "\\00" + octal;
        if (octal.length() == 2)
            return "\\0" + octal;
        return "\\" + octal;

    }

    public static void main(String[] args) {
        System.out.println(Integer.toString(29, 8));
    }
}
