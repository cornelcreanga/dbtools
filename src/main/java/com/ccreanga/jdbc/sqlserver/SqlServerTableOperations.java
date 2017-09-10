package com.ccreanga.jdbc.sqlserver;

import com.ccreanga.jdbc.BasicTableOperations;
import com.ccreanga.jdbc.model.DbConnection;

public class SqlServerTableOperations extends BasicTableOperations {
    @Override
    public long getTableSize(DbConnection connection, String schema, String table) {
        return 0;
    }

    @Override
    public long getAvgRowSize(DbConnection connection, String schema, String table) {
        return 0;
    }

    @Override
    public long getNoOfRows(DbConnection connection, String schema, String table) {
        return 0;
    }
}

