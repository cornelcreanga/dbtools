package com.ccreanga.jdbc.oracle;

import com.ccreanga.jdbc.BasicModelOperations;
import com.ccreanga.jdbc.model.DbConnection;

public class OracleOperations extends BasicModelOperations {
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
