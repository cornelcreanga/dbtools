package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.DbConnection;

public class PostgreSqlOperations extends BasicModelOperations {
    @Override
    public long getTableSize(DbConnection connection, String schema, String table) {
        Object data = singleResultQuery(connection,
                "select pg_relation_size(oid) from pg_class where relname ='"+table+"'");
        if (data==null)
            throw new RuntimeSqlException("cannot find table "+table+" in schema "+schema);
        return  ((Number)data).longValue();
    }

    @Override
    public long getAvgRowSize(DbConnection connection, String schema, String table) {
        Object data = singleResultQuery(connection,
                "select sum(avg_width) as average_row_size from pg_stats where tablename='"+table+"'");
        if (data==null)
            return -1;//data not available
        return  ((Number)data).longValue();
    }

    @Override
    public long getNoOfRows(DbConnection connection, String schema, String table) {
        Object data = singleResultQuery(connection,
                "select reltuples AS approximate_row_count from pg_class where relname ='"+table+"'");
        if (data==null)
            throw new RuntimeSqlException("cannot find table "+table+" in schema "+schema);
        return  ((Number)data).longValue();
    }

}
