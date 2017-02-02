package com.ccreanga.jdbc.oracle;

import com.ccreanga.jdbc.BasicTableOperations;
import com.ccreanga.jdbc.model.DbConnection;

public class OracleTableOperations extends BasicTableOperations {
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
