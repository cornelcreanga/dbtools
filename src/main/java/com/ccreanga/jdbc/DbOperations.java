package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.*;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface DbOperations {
    List<Schema> getSchemas(DbConnection connection);

    Optional<Table> getTable(DbConnection connection, String schema, String table);

    List<Table> getAllTables(DbConnection connection, String schema);

    List<Table> getTables(DbConnection connection, String schema, String tablePattern);

}
