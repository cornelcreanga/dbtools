package com.ccreanga.jdbc;

import com.ccreanga.jdbc.mysql.MySqlOperations;
import com.ccreanga.jdbc.oracle.OracleOperations;
import com.ccreanga.jdbc.postgresql.PostgreSqlOperations;

public class OperationsFactory {

    private OperationsFactory() {
    }

    public static Operations createOperations(Dialect dialect) {
        if (dialect == Dialect.MYSQL)
            return new MySqlOperations();
        if (dialect == Dialect.POSTGRESQL)
            return new PostgreSqlOperations();
        if (dialect == Dialect.ORACLE)
            return new OracleOperations();
        throw new IllegalArgumentException("unknown dialect " + dialect);
    }

}
