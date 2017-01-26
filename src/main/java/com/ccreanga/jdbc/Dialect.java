package com.ccreanga.jdbc;

public enum Dialect {

    MYSQL, POSTGRESQL, ORACLE, CASSANDRA;

    public boolean isSQL() {
        return this.equals(MYSQL) || this.equals(POSTGRESQL) || this.equals(ORACLE);
    }
}
