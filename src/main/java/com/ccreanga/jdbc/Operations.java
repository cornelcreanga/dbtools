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

    /**
     * Immediately closes the result set (without consuming all the remaining rows). Depending on the database server
     * any subsequent call might fail so after closing the result set this method will also close the database connection.
     * When to use it - if some fatal error occurs during a very large result set processing it does not make sense to wait until all the result
     * set is drained from server (as currently implemented in the MYSQL jdbc connector)
     * For the moment only MySQL forces you to
     * @param connection
     * @param rs
     */
    void forceDiscardResultSetAndCloseConnection(DbConnection connection, ResultSet rs);
}
