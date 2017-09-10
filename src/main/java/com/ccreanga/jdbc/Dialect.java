package com.ccreanga.jdbc;

public enum Dialect {

    MYSQL, POSTGRESQL, ORACLE, CASSANDRA,SQL_SERVER;

    public boolean isSQL() {
        return this.equals(MYSQL) || this.equals(POSTGRESQL) || this.equals(ORACLE) || this.equals(SQL_SERVER);
    }
}
