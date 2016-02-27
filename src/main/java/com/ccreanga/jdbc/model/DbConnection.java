package com.ccreanga.jdbc.model;

import com.ccreanga.jdbc.Dialect;
import lombok.Data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Data
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

}
