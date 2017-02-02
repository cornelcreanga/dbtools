package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.DbConnection;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;

public class RsBasicOperations implements RsOperations {

    public void forceDiscardResultSetAndCloseConnection(DbConnection connection, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                //ignore
            }
        }
        connection.close();
    }

    public Object readValue(ResultSet rs, int pos, int type) {
        try {
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
                case Types.NVARCHAR:
                case Types.VARCHAR: {
                    String s = rs.getString(pos);
                    return rs.wasNull() ? null : s;
                }
                case Types.NCHAR:
                case Types.CHAR:
                    String s = rs.getString(pos);
                    return rs.wasNull() ? null : s;
                case Types.DATE: {
                    Date d = rs.getDate(pos);
                    return rs.wasNull() ? null : d;
                }
                case Types.TIME: {
                    Time time = rs.getTime(pos);
                    //Timestamp t = rs.getTimestamp(pos);
                    //do not use rs.getTime(pos) as you'll lose the microseconds -todo - recheck
                    return rs.wasNull() ? null : time;
                }
                case -101://oracle
                case Types.TIMESTAMP: {
                    Timestamp t = rs.getTimestamp(pos);
                    return rs.wasNull() ? null : t;
                }
                case Types.LONGNVARCHAR:
                case Types.LONGVARCHAR: {
                    Clob clob = rs.getClob(pos);
                    return rs.wasNull() ? null : clob.getCharacterStream();
                }
                case Types.NCLOB:
                case Types.CLOB: {
                    Clob clob = rs.getClob(pos);
                    return rs.wasNull() ? null : clob.getCharacterStream();
                }
                case Types.BLOB: {
                    Blob blob = rs.getBlob(pos);
                    return rs.wasNull() ? null : blob.getBinaryStream();
                }
                case Types.LONGVARBINARY:
                case Types.VARBINARY:
                case Types.BINARY: {
                    InputStream in = rs.getBinaryStream(pos);
                    return rs.wasNull() ? null : in;                }
                case Types.DECIMAL: {
                    BigDecimal b = rs.getBigDecimal(pos);
                    return rs.wasNull() ? null : b;
                }
                case 100://oracle
                case Types.FLOAT: {
                    float d = rs.getFloat(pos);
                    return rs.wasNull() ? null : d;
                }
                case 101://oracle
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
                case Types.SQLXML: {
                    SQLXML sqlxml = rs.getSQLXML(pos);
                    return rs.wasNull() ? null : sqlxml.getString();
                }
                default:
                    throw new DatabaseException("unknown column type:" + type);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }

    }


}
