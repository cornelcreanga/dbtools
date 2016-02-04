package com.ccreanga.jdbc;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

public class StatementOperations {

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

}
