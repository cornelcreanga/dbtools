package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.*;

import java.util.List;
import java.util.function.Consumer;

public interface TableOperations {

    List<Column> getColumns(DbConnection connection, String schema, String table);

    List<Key> getTablePrimaryKeys(DbConnection connection, String schema, String table);

    List<Relation> getTableImportedKeys(DbConnection connection, String schema, String table);

    List<Relation> getTableExportedKeys(DbConnection connection, String schema, String table);

    long getTableSize(DbConnection connection, String schema, String table);

    long getAvgRowSize(DbConnection connection, String schema, String table);

    long getNoOfRows(DbConnection connection, String schema, String table);

    void processTableRows(DbConnection connection, Table table, List<Column> columns, Consumer<List<Object>> consumer);
}
