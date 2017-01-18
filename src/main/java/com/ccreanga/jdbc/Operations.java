package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.*;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface Operations {
    List<Schema> getSchemas(DbConnection connection);

    Optional<Table> getTable(DbConnection connection, String schema, String table);

    List<Table> getAllTables(DbConnection connection, String schema);

    List<Table> getTables(DbConnection connection, String schema, String tablePattern);

    List<Column> getColumns(DbConnection connection, String schema, String table);

    List<Key> getTablePrimaryKeys(DbConnection connection, String schema, String table);

    List<Relation> getTableImportedKeys(DbConnection connection, String schema, String table);

    List<Relation> getTableExportedKeys(DbConnection connection, String schema, String table);

    long getTableSize(DbConnection connection, String schema, String table);

    long getAvgRowSize(DbConnection connection, String schema, String table);

    long getNoOfRows(DbConnection connection, String schema, String table);

    void forceDiscardResultSet(DbConnection connection, ResultSet rs);
}
