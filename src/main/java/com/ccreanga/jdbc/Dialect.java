package com.ccreanga.jdbc;

public enum Dialect {

    MYSQL, POSTGRESQL, MONGODB, CASSANDRA;

    public boolean isSQL(){
        return this.equals(MYSQL) || this.equals(POSTGRESQL);
    }
}
