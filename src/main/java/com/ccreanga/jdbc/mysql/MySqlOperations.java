package com.ccreanga.jdbc.mysql;

import com.ccreanga.jdbc.BasicModelOperations;
import com.ccreanga.jdbc.RuntimeSqlException;
import com.ccreanga.jdbc.model.DbConnection;
import com.mysql.jdbc.JDBC42ResultSet;
import com.mysql.jdbc.MySQLConnection;
import com.mysql.jdbc.RowData;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlOperations extends BasicModelOperations {
    @Override
    public long getTableSize(DbConnection connection, String schema, String table) {
        Object data = singleResultQuery(connection,
                "select data_length from information_schema.tables where table_name ='" + table + "' and table_schema='" + schema + "'");
        if (data == null)
            throw new RuntimeSqlException("cannot find table " + table + " in schema " + schema);
        return ((Number) data).longValue();
    }

    @Override
    public long getAvgRowSize(DbConnection connection, String schema, String table) {
        Object data = singleResultQuery(connection,
                "select avg_row_length from information_schema.tables where table_name ='" + table + "' and table_schema='" + schema + "'");
        if (data == null)
            throw new RuntimeSqlException("cannot find table " + table + " in schema " + schema);
        return ((Number) data).longValue();
    }

    @Override
    public long getNoOfRows(DbConnection connection, String schema, String table) {
        Object data = singleResultQuery(connection,
                "select table_rows from information_schema.tables where table_name='" + table + "' and table_schema='" + schema + "'");
        if (data == null)
            throw new RuntimeSqlException("cannot find table " + table + " in schema " + schema);
        return ((Number) data).longValue();
    }

    @Override
    public void forceDiscardResultSet(DbConnection connection, ResultSet rs) {
        if (!(rs instanceof JDBC42ResultSet)) {
            super.forceDiscardResultSet(connection, rs);
            connection.close();
        }
        else {
            try {
                Class<?> rsClass = rs.getClass().getSuperclass().getSuperclass();
                Field rowdata = rsClass.getDeclaredField("rowData");
                rowdata.setAccessible(true);
                RowData rd = (RowData) rowdata.get(rs);

                Class<?> rdClass = rd.getClass();
                Field noMoreRowsField = rdClass.getDeclaredField("noMoreRows");
                noMoreRowsField.setAccessible(true);
                noMoreRowsField.set(rd, true);
                try {
                    ((MySQLConnection) connection.getConnection()).setNetTimeoutForStreamingResults(0);
                    connection.close();//the protocol is broken in this moment the only 'sane' operations is close
                }catch (SQLException e){
                    //ignore any exception thrown
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                //revert to the standard way
                super.forceDiscardResultSet(connection, rs);
                connection.close();

            }

        }
    }
}
