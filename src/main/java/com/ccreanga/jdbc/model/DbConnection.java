package com.ccreanga.jdbc.model;

import com.ccreanga.jdbc.Dialect;
import lombok.Data;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Data
public class DbConnection {

    private final Connection connection;
    private final Dialect dialect;

    public DatabaseMetaData meta() throws SQLException {
        return connection.getMetaData();
    }

}
