package com.ccreanga.jdbc.model;

import com.ccreanga.jdbc.Dialect;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class DbConnection implements AutoCloseable {

    private final Connection connection;
    private final Dialect dialect;

    public DatabaseMetaData meta() throws SQLException {
        return connection.getMetaData();
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            //ignored
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DbConnection that = (DbConnection) o;

        if (connection != null ? !connection.equals(that.connection) : that.connection != null) return false;
        return dialect == that.dialect;

    }

    @Override
    public int hashCode() {
        int result = connection != null ? connection.hashCode() : 0;
        result = 31 * result + (dialect != null ? dialect.hashCode() : 0);
        return result;
    }

    public DbConnection(Connection connection, Dialect dialect) {
        this.connection = connection;
        this.dialect = dialect;
    }

    public Connection getConnection() {
        return connection;
    }

    public Dialect getDialect() {
        return dialect;
    }
}
