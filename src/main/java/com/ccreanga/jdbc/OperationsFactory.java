package com.ccreanga.jdbc;

public class OperationsFactory {

    private OperationsFactory(){}

    public static Operations createOperations(Dialect dialect){
        if (dialect==Dialect.MYSQL)
            return new MySqlOperations();
        if (dialect==Dialect.POSTGRESQL)
            return new PostgreSqlOperations();
        throw new IllegalArgumentException("uknown dialect "+dialect);
    }

}
