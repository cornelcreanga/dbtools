package com.ccreanga.jdbc;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;

public class ResultSetOperations {

    //https://dev.mysql.com/doc/connector-j/en/connector-j-reference-type-conversions.html
    public static Object readValue(ResultSet rs, int pos, int type) throws Exception {

        switch (type) {
            case Types.BIT: {
                boolean b = rs.getBoolean(pos);
                return rs.wasNull() ? null : b ? Boolean.TRUE : Boolean.FALSE;
            }
            case Types.BOOLEAN: {
                boolean b = rs.getBoolean(pos);
                return rs.wasNull() ? null : b ? Boolean.TRUE : Boolean.FALSE;
            }
            case Types.BIGINT: {
                long l = rs.getLong(pos);
                return rs.wasNull() ? null : l;
            }
            case Types.INTEGER: {
                int i = rs.getInt(pos);
                return rs.wasNull() ? null : i;
            }
            case Types.VARCHAR: {
                String s = rs.getString(pos);
                return rs.wasNull()?null:s;
            }
            case Types.CHAR:
                String s = rs.getString(pos);
                return rs.wasNull()?null:s;
            case Types.DATE: {
                Date d = rs.getDate(pos);
                return rs.wasNull()?null:d;
            }
            case Types.TIME: {
                Time t = rs.getTime(pos);
                return rs.wasNull()?null:t;
            }
            case Types.TIMESTAMP: {
                Timestamp t = rs.getTimestamp(pos);
                return rs.wasNull()?null:t;
            }
            case Types.LONGVARCHAR: {
                Clob clob = rs.getClob(pos);
                return rs.wasNull()?null:readClob(clob);
            }
            case Types.CLOB: {
                Clob clob = rs.getClob(pos);
                return rs.wasNull()?null:readClob(clob);
            }
            case Types.BLOB: {
                Blob blob = rs.getBlob(pos);
                return rs.wasNull()?null:readBlob(blob);
            }
            case Types.LONGVARBINARY: {
                InputStream reader = rs.getBinaryStream(pos);
                return rs.wasNull()?null:readBinary(reader);
            }
            case Types.VARBINARY: {
                InputStream reader = rs.getBinaryStream(pos);
                return rs.wasNull()?null:readBinary(reader);

            }
            case Types.BINARY: {
                InputStream reader = rs.getBinaryStream(pos);
                return rs.wasNull()?null:readBinary(reader);

            }
            case Types.DECIMAL: {
                BigDecimal b = rs.getBigDecimal(pos);
                return rs.wasNull() ? null : b;
            }
            case Types.FLOAT: {
                float d = rs.getFloat(pos);
                return rs.wasNull() ? null : d;
            }
            case Types.DOUBLE: {
                double d = rs.getDouble(pos);
                return rs.wasNull() ? null : d;
            }
            case Types.REAL: {
                double d = rs.getDouble(pos);
                return rs.wasNull() ? null : d;
            }

            case Types.SMALLINT: {
                short sh = rs.getShort(pos);
                return rs.wasNull() ? null : sh;
            }
            case Types.TINYINT: {
                byte b = rs.getByte(pos);
                return rs.wasNull() ? null : b;
            }
            case Types.NUMERIC: {
                BigDecimal b = rs.getBigDecimal(pos);
                return rs.wasNull() ? null : b;
            }
            default:
                throw new RuntimeException("cannot read value;unknown type:" + type);
        }

    }

    public static void readClob(Clob clob, Writer writer) throws Exception {

        char[] buffer = new char[4 * 1024];
        int count = 0;
        Reader reader = clob.getCharacterStream();
        while ((count = reader.read(buffer)) >= 0) {
            writer.write(buffer, 0, count);
        }
    }

    public static void readCharacterStream(Reader reader, Writer writer) throws Exception {

        char[] buffer = new char[4 * 1024];
        int count;
        if (reader != null) {
            while ((count = reader.read(buffer)) >= 0) {
                writer.write(buffer, 0, count);
            }
        }
    }

    public static String readClob(Clob clob) throws Exception {
        StringWriter writer = new StringWriter();
        readClob(clob,writer);
        return writer.toString();
    }

    public static String readCharacterStream(Reader reader) throws Exception {
        StringWriter writer = new StringWriter();
        readCharacterStream(reader, writer);
        return writer.toString();
    }

    public static void readBlob(Blob blob ,OutputStream out) throws Exception {
        byte[] buffer = new byte[4 * 1024];
        int count;
        InputStream reader = blob.getBinaryStream();
        while ((count = reader.read(buffer)) >= 0) {
            out.write(buffer, 0, count);
        }
    }

    public static void readBinary(InputStream reader, OutputStream out) throws Exception {

        byte[] buffer = new byte[4 * 1024];
        int count;
        if (reader != null) {
            while ((count = reader.read(buffer)) >= 0) {
                out.write(buffer, 0, count);
            }
        }
    }

    public static byte[] readBinary(InputStream reader) throws Exception {

        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        readBinary(reader, writer);
        return writer.toByteArray();
    }

    public static byte[] readBlob(Blob blob) throws Exception {

        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        readBlob(blob, writer);
        return writer.toByteArray();
    }
}