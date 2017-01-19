package com.ccreanga.jdbc;


import com.ccreanga.jdbc.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BasicModelOperations implements Operations {

    @Override
    public List<Schema> getSchemas(DbConnection connection) {
        List<Schema> schemas = new ArrayList<>(16);
        try (ResultSet rs = connection.meta().getCatalogs()) {
            while (rs.next()) {
                schemas.add(new Schema(rs.getString(1)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        try (ResultSet rs = connection.meta().getTables(schema, "%", tablePattern, new String[]{"TABLE"})) {
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
        if (connection.getDialect().equals(Dialect.MYSQL)) {
            return connection.meta().getCatalogs();
        }
        throw new IllegalArgumentException("unhandled dialect " + connection.getDialect());
    }

    @Override
    public List<Column> getColumns(DbConnection connection, String schema, String table) {

        List<Column> columns = new ArrayList<>(16);
        try (ResultSet rs = connection.meta().getColumns(schema, "%", table, "%");) {
            while (rs.next()) {
                Column column = new Column(
                        rs.getString(1),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getShort(5),
                        rs.getString(6),
                        rs.getInt(7),
                        rs.getInt(9),
                        rs.getInt(10),
                        rs.getInt(11),
                        rs.getString(12),
                        rs.getString(13),
                        rs.getInt(16),
                        rs.getInt(17),
                        rs.getString(18));
                columns.add(column);
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        return columns;
    }

    @Override
    public List<Key> getTablePrimaryKeys(DbConnection connection, String schema, String table) {
        List<Key> keys = new ArrayList<>(16);
        //todo
        try (ResultSet rs = connection.meta().getPrimaryKeys(null, null, table)) {
            while (rs.next()) {
                keys.add(new Key(rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
        return keys;
    }

    private List<Relation> buildRelations(ResultSet rs) throws SQLException {
        List<Relation> relations = new ArrayList<>(4);
        while (rs.next()) {
            Key primaryKey = new Key(rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(9), rs.getString(13));
            Key foreignKey = new Key(rs.getString(5), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(12));
            Relation relation = new Relation(primaryKey, foreignKey, rs.getShort(10), rs.getShort(11), rs.getShort(14));
            relations.add(relation);
        }
        return relations;
    }

    @Override
    public List<Relation> getTableImportedKeys(DbConnection connection, String schema, String table) {
        try (ResultSet rs = connection.meta().getImportedKeys(schema, "%", table)) {
            return buildRelations(rs);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Relation> getTableExportedKeys(DbConnection connection, String schema, String table) {
        try (ResultSet rs = connection.meta().getExportedKeys(schema, "%", table)) {
            return buildRelations(rs);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }


    protected Object singleResultQuery(DbConnection connection, String query) {
        try (Statement st = connection.getConnection().createStatement()) {
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                return rs.getObject(1);
            }
            return null;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void forceDiscardResultSetAndCloseConnection(DbConnection connection, ResultSet rs){
        if (rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                //ignore
            }
        }
        connection.close();
    }
}
