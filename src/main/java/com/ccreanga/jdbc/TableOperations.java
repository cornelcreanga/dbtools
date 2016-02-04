package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Table;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TableOperations {

    public static void setValue(PreparedStatement ps, int pos, int type, Object value) throws Exception {
        try {
            switch (type) {
                case Types.BIT: {
                    if (value == null) {
                        ps.setNull(pos, Types.BIT);
                    } else {
                        ps.setBoolean(pos, (Boolean)value);
                    }
                    break;
                }
                case Types.BOOLEAN: {
                    if (value == null) {
                        ps.setNull(pos, Types.BOOLEAN);
                    } else {
                        ps.setBoolean(pos, (Boolean)value);
                    }
                    break;
                }
                case Types.BIGINT: {
                    if (value == null) {
                        ps.setNull(pos, Types.BIGINT);
                    } else {
                        ps.setLong(pos, (Long)value);
                    }
                    break;
                }
                case Types.INTEGER: {
                    if (value == null) {
                        ps.setNull(pos, Types.INTEGER);
                    } else {
                        ps.setInt(pos, (Integer)value);
                    }
                    break;
                }
                case Types.VARCHAR: {
                    if (value == null) {
                        ps.setNull(pos, Types.VARCHAR);
                    } else {
                        ps.setString(pos, (String) value);
                    }
                    break;
                }
                case Types.CHAR: {
                    if (value == null) {
                        ps.setNull(pos, Types.CHAR);
                    } else {
                        ps.setString(pos, (String) value);
                    }
                    break;
                }
                case Types.DATE: {
                    if (value == null) {
                        ps.setNull(pos, Types.DATE);
                    } else {
                        ps.setDate(pos, (Date)value);
                    }
                    break;
                }
                case Types.TIME: {
                    if (value == null) {
                        ps.setNull(pos, Types.TIME);
                    } else {
                        ps.setTime(pos, (Time)value);
                    }
                    break;
                }
                case Types.TIMESTAMP: {
                    if (value == null) {
                        ps.setNull(pos, Types.TIMESTAMP);
                    } else {
                        ps.setTimestamp(pos, (Timestamp)value);
                    }
                    break;
                }
                case Types.LONGVARCHAR: {
                    if (value == null) {
                        ps.setNull(pos, Types.LONGVARCHAR);
                    } else {
                        writeCharacterStream(ps, pos, (String)value);
                    }
                    break;
                }
                case Types.LONGVARBINARY: {
                    if (value == null) {
                        ps.setNull(pos, Types.LONGVARBINARY);
                    } else {
                        writeBinary(ps, pos, (byte[]) value);
                    }
                    break;

                }
                case Types.CLOB: {
                    if (value == null) {
                        ps.setNull(pos, Types.CLOB);
                    } else {//todo
                        writeCharacterStream(ps, pos, (String)value);
                    }
                    break;

                }
                case Types.BLOB: {
                    if (value == null) {
                        ps.setNull(pos, Types.BLOB);
                    } else {
                        writeBinary(ps, pos, (byte[]) value);
                    }
                    break;
                }

                case Types.BINARY: {
                    if (value == null) {
                        ps.setNull(pos, Types.BINARY);
                    } else {
                        writeBinary(ps, pos, (byte[]) value);
                    }
                    break;
                }

                case Types.VARBINARY: {
                    if (value == null) {
                        ps.setNull(pos, Types.BINARY);
                    } else {
                        writeBinary(ps, pos, (byte[]) value);
                    }
                    break;
                }

                case Types.DECIMAL: {
                    if (value == null) {
                        ps.setNull(pos, Types.DECIMAL);
                    } else {
                        ps.setBigDecimal(pos, (BigDecimal)value);
                    }
                    break;
                }
                case Types.FLOAT: {
                    if (value == null) {
                        ps.setNull(pos, Types.FLOAT);
                    } else {
                        ps.setFloat(pos, (Float)value);
                    }
                    break;
                }
                case Types.DOUBLE: {
                    if (value == null) {
                        ps.setNull(pos, Types.DOUBLE);
                    } else {
                        ps.setDouble(pos, (Double)value);
                    }
                    break;
                }
                case Types.SMALLINT: {
                    if (value == null) {
                        ps.setNull(pos, Types.SMALLINT);
                    } else {
                        ps.setShort(pos, (Short)value);
                    }
                    break;
                }
                case Types.TINYINT: {
                    if (value == null) {
                        ps.setNull(pos, Types.TINYINT);
                    } else {
                        ps.setByte(pos, (Byte)value);
                    }
                    break;
                }
                case Types.NUMERIC: {
                    if (value == null) {
                        ps.setNull(pos, Types.NUMERIC);
                    } else {
                        ps.setBigDecimal(pos, (BigDecimal)value);
                    }
                    break;
                }

                default:
                    throw new RuntimeException("cannot read value;unknown type");
            }

        } catch (Exception e) {
            throw e;
        } finally {

        }
    }

    public static void writeCharacterStream(PreparedStatement ps, int index, String data) throws SQLException {
        StringReader reader = new StringReader(data);
        ps.setCharacterStream(index, reader, data.length());
    }

    public static void writeBinary(PreparedStatement ps, int index, byte[] bytes) throws SQLException {
        ps.setBinaryStream(index, new ByteArrayInputStream(bytes), bytes.length);
    }


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

    public void processTableRows(DbConnection connection, Table table, Consumer<List<Object>> consumer) {

        String selectData = "select " + String.join(",", table.getColumns().stream().map(Column::getName).collect(Collectors.toList())) + " from " + table.getName();
        //selectData +=" where id=12";
        long t1 = System.currentTimeMillis();
        long totalTime = 0, startTime = t1;

        DecimalFormat df = new DecimalFormat("###.###");
        df.setGroupingUsed(true);
        df.setGroupingSize(3);


        try (Statement st = connection.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            st.setFetchSize(Integer.MIN_VALUE);//todo - this is just for mysql!
            ResultSet rs = st.executeQuery(selectData);
            int colCount = rs.getMetaData().getColumnCount();
            int types[] = new int[colCount];
            for (int i = 0; i < types.length; i++) {
                types[i] = rs.getMetaData().getColumnType(i + 1);
            }
            int counter=1;
            while (rs.next()) {
                List<Object> line = new ArrayList<>(colCount);
                for (int i = 0; i < colCount; i++) {
                    try {
                        line.add(readValue(rs, i + 1, types[i]));
                    } catch (Exception e) {
                        throw new DatabaseException(e);
                    }
                }
                consumer.accept(line);
                if (counter%10000==0){
                    long time = System.currentTimeMillis() - t1;
                    t1 = System.currentTimeMillis();
                    totalTime = System.currentTimeMillis() - startTime;
                    String message =  "\rProcessing 10000 rows in " + df.format((double)time/1000) + " seconds, total processed lines="+df.format(counter)+", total time="+df.format((double)totalTime/1000);
                    System.out.print(message);
                }
                counter++;
            }


        } catch (Exception e) {
            throw new DatabaseException(e);
        }

    }

}
