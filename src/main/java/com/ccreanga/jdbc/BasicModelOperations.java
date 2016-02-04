package com.ccreanga.jdbc;


import com.ccreanga.jdbc.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BasicModelOperations {


    public List<Schema> getSchemas(DbConnection connection) {
        List<Schema> schemas = new ArrayList<>(16);
        try (ResultSet rs = connection.meta().getCatalogs()) {
            while(rs.next()) {
                schemas.add(new Schema(rs.getString(1)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return schemas;
    }

    public Optional<Table> getTable(DbConnection connection, String schema, String table) {
        List<Table> list = getTables(connection,schema,table);
        if (list.size()==0)
            return Optional.empty();
        return Optional.of(list.get(0));
    }

    public List<Table> getTables(DbConnection connection, String schema) {
        return getTables(connection,schema,"%");
    }

    public List<Table> getTables(DbConnection connection, String schema,String tablePattern) {
        List<Table> tables = new ArrayList<>(16);
        try (ResultSet rs = connection.meta().getTables(schema, "%", tablePattern, new String[]{"TABLE"})) {
            while(rs.next()) {
                String tableName = rs.getString(3);
                Table table = new Table(
                        rs.getString(1),
                        tableName,
                        rs.getString(4),
                        rs.getString(5),
                        getTablePrimaryKeys(connection, schema, tableName),
                        getColumns(connection, schema, tableName),
                        getTableImportedKeys(connection, schema, tableName),
                        getTableExportedKeys(connection, schema, tableName),
                        null
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

    private List<Key> getTablePrimaryKeys(DbConnection connection, String schema, String table) {
        List<Key> keys = new ArrayList<>(16);
        try (ResultSet rs = connection.meta().getPrimaryKeys(schema, "%", table)) {
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
    
    private List<Relation> getTableImportedKeys(DbConnection connection, String schema, String table) {
        try (ResultSet rs = connection.meta().getImportedKeys(schema, "%", table)) {
            return buildRelations(rs);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    private List<Relation> getTableExportedKeys(DbConnection connection, String schema, String table) {
        try (ResultSet rs = connection.meta().getExportedKeys(schema, "%", table)) {
            return buildRelations(rs);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }


//    public static void main(String[] args) throws Exception {
////        Class.forName("com.mysql.jdbc.Driver");
////        Connection connectionIn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?user=root&password=root");
////        _tmpExporter exporter = new _tmpExporter();
////
////        MySQLCSVWriterConsumer mySQLCSVWriter = new MySQLCSVWriterConsumer("/tmp/test2.txt");
////        exporter.exportData(connectionIn, Dialect.MYSQL, new BasicModelOperations().getTables(connectionIn, "test", Dialect.MYSQL), mySQLCSVWriter);
////        mySQLCSVWriter.close();
////        Connection connectionIn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/web_vbp_prod", "cornel", "cornel");
////        connectionIn.setAutoCommit(false);
////        Connection connectionOut = DriverManager.getConnection("jdbc:postgresql://localhost:5432/web_vbp_dev", "cornel", "cornel");
////        connectionOut.setAutoCommit(false);
//
//
////        Connection connectionIn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?user=root&password=root");
////        Database db  = new Database(connectionIn);
////        System.out.println(db);
//    }
}
