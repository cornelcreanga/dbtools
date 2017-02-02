package com.ccreanga.jdbc;

import com.ccreanga.GenericConfig;
import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.jdbc.model.*;
import com.ccreanga.util.FormatUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.ccreanga.util.FormatUtil.formatMillis;
import static com.ccreanga.util.FormatUtil.readableSize;

public abstract class BasicTableOperations implements TableOperations {

    @Override
    public List<Column> getColumns(DbConnection connection, String schema, String table) {

        List<Column> columns = new ArrayList<>(16);
        try (ResultSet rs = connection.meta().getColumns(
                catalog(schema,connection.getDialect()),
                schema(schema,connection.getDialect()),
                table,
                "%");) {
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
        try (ResultSet rs = connection.meta().getPrimaryKeys(
                catalog(schema,connection.getDialect()),
                schema(schema,connection.getDialect()),
                table)) {
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
        try (ResultSet rs = connection.meta().getImportedKeys(
                catalog(schema,connection.getDialect()),
                schema(schema,connection.getDialect()),
                table)) {
            return buildRelations(rs);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public List<Relation> getTableExportedKeys(DbConnection connection, String schema, String table) {
        try (ResultSet rs = connection.meta().getExportedKeys(
                catalog(schema,connection.getDialect()),
                schema(schema,connection.getDialect()),
                table)) {
            return buildRelations(rs);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }


    @Override
    public void processTableRows(DbConnection connection, Table table, List<Column> columns, Consumer<List<Object>> consumer) {
        String selectData = String.format("select %s from %s",
                String.join(",", columns.stream().map(Column::getName).collect(Collectors.toList())),
                table.getName());
        long totalTime = 0, t1 = System.currentTimeMillis(), startTime = t1;

        //try to use statistics
        RsOperations rsOperations = OperationsFactory.createRsOperations(connection.getDialect());
        long tableSize = getTableSize(connection, table.getSchema(), table.getName());
        long tableRows = getNoOfRows(connection, table.getSchema(), table.getName());

        System.out.println("Table size is " + readableSize(tableSize) + ", estimated number of rows is " + FormatUtil.readableSize(tableRows));

        boolean close = true;
        ResultSet rs = null;
        Statement st = null;
        try {
            st = connection.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (connection.getDialect() == Dialect.MYSQL)
                st.setFetchSize(Integer.MIN_VALUE);//enable mysql streaming
            else
                st.setFetchSize(100);
            rs = st.executeQuery(selectData);
            int colCount = rs.getMetaData().getColumnCount();
            int types[] = new int[colCount];
            for (int i = 0; i < types.length; i++) {
                types[i] = rs.getMetaData().getColumnType(i + 1);
            }
            int counter = 1;
            while (rs.next()) {
                List<Object> line = new ArrayList<>(colCount);
                for (int i = 0; i < colCount; i++) {
                    line.add(rsOperations.readValue(rs, i + 1, types[i]));
                }
                consumer.accept(line);
                if (counter % GenericConfig.progress == 0) {
                    long time = System.currentTimeMillis() - t1;
                    t1 = System.currentTimeMillis();
                    totalTime = System.currentTimeMillis() - startTime;
                    System.out.printf("\rProcessing %s rows in %s seconds, total processed lines=%s, total time=%s seconds.",
                            GenericConfig.progress,
                            formatMillis(time),
                            readableSize(counter),
                            formatMillis(totalTime)
                    );
                }
                counter++;
            }
            if (counter == 1)
                System.out.print("\rNo rows found");
            else {
                totalTime = System.currentTimeMillis() - startTime;
                System.out.printf("\rProcessed %s rows in %s seconds.\n",
                        readableSize(counter - 1),
                        formatMillis(totalTime));

            }

        } catch (IOExceptionRuntime e) {
            rsOperations.forceDiscardResultSetAndCloseConnection(connection, rs);
            close = false;
            throw e;
            //force discard rs, close connection
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            try {
                if ((st != null) && close)
                    st.close();
            } catch (SQLException e) {
            }
        }

    }

    private String schema(String schema,Dialect dialect){
        if (dialect==Dialect.ORACLE)
            return schema.toUpperCase();
        return "%";
    }

    private String catalog(String schema, Dialect dialect){
        if (dialect==Dialect.ORACLE)
            return null;
        return schema;
    }


}
