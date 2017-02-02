package com.ccreanga.jdbc;


import com.ccreanga.jdbc.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BasicDbOperations implements DbOperations {

    @Override
    public List<Schema> getSchemas(DbConnection connection) {
        List<Schema> schemas = new ArrayList<>(16);
        try (ResultSet rs = connection.meta().getCatalogs()) {
            while (rs.next()) {
                schemas.add(new Schema(rs.getString(1)));
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        return schemas;
    }

    @Override
    public Optional<Table> getTable(DbConnection connection, String schema, String table) {
        List<Table> list = getTables(connection, schema, table);
        if (list.size() == 0)
            return Optional.empty();
        return Optional.of(list.get(0));
    }

    @Override
    public List<Table> getAllTables(DbConnection connection, String schema) {
        return getTables(connection, schema, "%");
    }

    @Override
    public List<Table> getTables(DbConnection connection, String schema, String tablePattern) {
        List<Table> tables = new ArrayList<>(16);
        try (ResultSet rs = connection.meta().getTables(
                JdbcUtil.catalog(schema,connection.getDialect()),
                JdbcUtil.schema(schema,connection.getDialect()),
                tablePattern,
                new String[]{"TABLE"})) {
            while (rs.next()) {
                String tableName = rs.getString(3);
                Table table = new Table(
                        rs.getString(1),
                        tableName,
                        rs.getString(4),
                        rs.getString(5)
                );
                tables.add(table);
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        return tables;
    }


    private ResultSet getSchemaRs(DbConnection connection) throws SQLException {
        if (connection.getDialect()==Dialect.MYSQL) {
            return connection.meta().getCatalogs();
        }
        throw new IllegalArgumentException("unhandled dialect " + connection.getDialect());
    }


}
