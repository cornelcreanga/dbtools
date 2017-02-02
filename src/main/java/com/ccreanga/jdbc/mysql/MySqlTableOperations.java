package com.ccreanga.jdbc.mysql;

import com.ccreanga.jdbc.BasicTableOperations;
import com.ccreanga.jdbc.JdbcUtil;
import com.ccreanga.jdbc.RuntimeSqlException;
import com.ccreanga.jdbc.model.DbConnection;

public class MySqlTableOperations extends BasicTableOperations {
    @Override
    public long getTableSize(DbConnection connection, String schema, String table) {
        Object data = JdbcUtil.singleResultQuery(connection,
                "select data_length from information_schema.tables where table_name ='" + table + "' and table_schema='" + schema + "'");
        if (data == null)
            throw new RuntimeSqlException("cannot find table " + table + " in schema " + schema);
        return ((Number) data).longValue();
    }

    @Override
    public long getAvgRowSize(DbConnection connection, String schema, String table) {
        Object data = JdbcUtil.singleResultQuery(connection,
                "select avg_row_length from information_schema.tables where table_name ='" + table + "' and table_schema='" + schema + "'");
        if (data == null)
            throw new RuntimeSqlException("cannot find table " + table + " in schema " + schema);
        return ((Number) data).longValue();
    }

    @Override
    public long getNoOfRows(DbConnection connection, String schema, String table) {
        Object data = JdbcUtil.singleResultQuery(connection,
                "select table_rows from information_schema.tables where table_name='" + table + "' and table_schema='" + schema + "'");
        if (data == null)
            throw new RuntimeSqlException("cannot find table " + table + " in schema " + schema);
        return ((Number) data).longValue();
    }
}
