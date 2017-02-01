package com.ccreanga.usecases.export.jdbc.oracle;

import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.jdbc.oracle.OracleScriptGenerator;
import com.ccreanga.util.Encoding;
import com.ccreanga.util.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class OracleCSVConvertor {

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    DateTimeFormatter timeStampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");

    public OracleCSVConvertor() {
    }

    public String toStringConvertor(Object value,OutputStream lobStream,String boundary) {
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
                IOUtil.copy((InputStream)value,lobStream);
                lobStream.write(boundary.getBytes());
                return "";
            } catch (IOException e) {
                throw new IOExceptionRuntime(e);
            }
        }

        if (value instanceof Reader) {
            try {
                IOUtil.copy((Reader)value,lobStream);
                lobStream.write(boundary.getBytes());
                return "";
            } catch (IOException e) {
                throw new IOExceptionRuntime(e);
            }
        }

        throw new RuntimeException("cannot convert value;unknown type:" + value.getClass());
    }


}
