package com.ccreanga.jdbc;

import com.ccreanga.jdbc.mysql.MySqlDbOperations;
import com.ccreanga.jdbc.mysql.MySqlRsOperations;
import com.ccreanga.jdbc.mysql.MySqlTableOperations;
import com.ccreanga.jdbc.oracle.OracleDbOperations;
import com.ccreanga.jdbc.oracle.OracleTableOperations;
import com.ccreanga.jdbc.postgresql.PostgreSqlDbOperations;
import com.ccreanga.jdbc.postgresql.PostgreSqlTableOperations;
import com.ccreanga.jdbc.sqlserver.SqlServerDbOperations;
import com.ccreanga.jdbc.sqlserver.SqlServerTableOperations;

public class OperationsFactory {

    private OperationsFactory() {
    }

    public static DbOperations createDbOperations(Dialect dialect) {
        if (dialect == Dialect.MYSQL)
            return new MySqlDbOperations();
        if (dialect == Dialect.POSTGRESQL)
            return new PostgreSqlDbOperations();
        if (dialect == Dialect.ORACLE)
            return new OracleDbOperations();
        if (dialect == Dialect.SQL_SERVER)
            return new SqlServerDbOperations();

        throw new IllegalArgumentException("unknown dialect " + dialect);
    }

    public static TableOperations createTableOperations(Dialect dialect) {
        if (dialect == Dialect.MYSQL)
            return new MySqlTableOperations();
        if (dialect == Dialect.POSTGRESQL)
            return new PostgreSqlTableOperations();
        if (dialect == Dialect.ORACLE)
            return new OracleTableOperations();
        if (dialect == Dialect.SQL_SERVER)
            return new SqlServerTableOperations();
        throw new IllegalArgumentException("unknown dialect " + dialect);
    }


    public static RsOperations createRsOperations(Dialect dialect) {
        if (dialect == Dialect.MYSQL)
            return new MySqlRsOperations();
        if (dialect == Dialect.POSTGRESQL)
            return new RsBasicOperations();
        if (dialect == Dialect.ORACLE)
            return new RsBasicOperations();
        if (dialect == Dialect.SQL_SERVER)
            return new RsBasicOperations();
        throw new IllegalArgumentException("unknown dialect " + dialect);
    }


}
