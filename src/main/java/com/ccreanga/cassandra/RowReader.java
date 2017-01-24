package com.ccreanga.cassandra;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.LocalDate;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.utils.Bytes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RowReader {

    public List<Object> readRow(Row row,List<ColumnMetadata> columns){
        List<Object> results = new ArrayList<>(columns.size());
        for (int i = 0; i < columns.size(); i++) {
            DataType type = columns.get(i).getType();
            switch(type.getName()){
                case ASCII:
                    results.add(row.getString(i));break;
                case BIGINT:
                    results.add(row.getLong(i));break;
                case BLOB:
                    results.add(row.getBytes(i).array());break;
                case BOOLEAN:
                    results.add(row.getBool(i));break;
                case DATE:
                    results.add(row.getDate(i));break;
                case DECIMAL:
                    results.add(row.getDecimal(i));break;
                case DOUBLE:
                    results.add(row.getDouble(i));break;
                case FLOAT:
                    results.add(row.getFloat(i));break;
                case INET:
                    results.add(row.getInet(i));break;
                case INT:
                    results.add(row.getInt(i));break;
                case LIST:
                    results.add(row.getList(i,get(type.getTypeArguments().get(0))));break;
                case MAP:
                    results.add(row.getMap(i,get(type.getTypeArguments().get(0)),get(type.getTypeArguments().get(1))));break;
                case SET:
                    results.add(row.getSet(i,get(type.getTypeArguments().get(0))));break;
                case SMALLINT:
                    results.add(row.getShort(i));break;
                case TEXT:
                    results.add(row.getString(i));break;
                case TIME:
                    results.add(new Time(row.getTime(i)));break;
                case TIMESTAMP:
                    results.add(row.getTimestamp(i));break;
                case UUID:
                    results.add(row.getUUID(i));break;
                case TIMEUUID:
                    results.add(row.getUUID(i));break;
                case TINYINT:
                    results.add(row.getByte(i));break;
                case VARCHAR:
                    results.add(row.getString(i));break;
                case VARINT:
                    results.add(row.getVarint(i));break;
                default:
                    throw new RuntimeException("unknow type "+type.getName());
            }
        }
        return results;
    }

    private Class get(DataType type){
        switch (type.getName()){
            case ASCII:
                return String.class;
            case BIGINT:
                return Long.class;
            case BLOB://todo
            case BOOLEAN:
                return Boolean.class;
            case DATE:
                return LocalDate.class;
            case DECIMAL:
                return BigDecimal.class;
            case DOUBLE:
                return Double.class;
            case FLOAT:
                return Float.class;
            case INET:
                return InetAddress.class;
            case INT:
                return Integer.class;
            case SMALLINT:
                return Short.class;
            case TEXT:
                return String.class;
            case TIME:
                return Long.class;
            case TIMESTAMP:
                return Date.class;
            case UUID:
                return UUID.class;
            case TINYINT:
                return Byte.class;
            case VARCHAR:
                return String.class;
            case VARINT:
                return BigInteger.class;
        }
        throw new RuntimeException("unhandled type "+type.getName());
    }

}
