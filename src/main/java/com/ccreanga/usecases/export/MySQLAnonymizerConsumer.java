package com.ccreanga.usecases.export;

import com.ccreanga.GenericConfig;
import com.ccreanga.anonymizer.Anonymizer;
import com.ccreanga.jdbc.DatabaseException;
import com.ccreanga.jdbc.StatementOperations;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Table;

import java.io.Closeable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MySQLAnonymizerConsumer implements Consumer<List<Object>>, Closeable {

    private DataAnonymizer anonymizer;
    private DbConnection writeConnection;
    private Table table;
    private List<Column> columns;
    private List<Column> primaryKeyColumns;
    private int counter = 1;
    private PreparedStatement ps;

    public MySQLAnonymizerConsumer(DataAnonymizer anonymizer, DbConnection writeConnection, Table table, List<Column> columns, List<Column> primaryKeyColumns) {
        this.anonymizer = anonymizer;
        this.writeConnection = writeConnection;
        this.table = table;
        this.columns = columns;
        this.primaryKeyColumns = primaryKeyColumns;
    }

    @Override
    public void close() {
        try {
            ps.close();
        } catch (SQLException e) {
            //ignore
        }
    }

    @Override
    public void accept(List<Object> objects) {
        if (ps == null)
            throw new IllegalArgumentException("this consumer was not initialized");
        try {
            int i;
            for (i = 0; i < columns.size(); i++) {
                Object value = objects.get(i);
                Object valueToWrite = value;
                if (value != null) {
                    Optional<Anonymizer> optional = anonymizer.getAnonymizer(table.getName(), columns.get(i).getName());
                    if (optional.isPresent()) {
                        valueToWrite = optional.get().anonymize(value);
                    }
                }
                StatementOperations.setValue(ps, i + 1, columns.get(i).getType(), valueToWrite);
            }
            for (int j = 0; j < primaryKeyColumns.size(); j++) {
                StatementOperations.setValue(ps, i + j + 1, primaryKeyColumns.get(j).getType(), objects.get(i + j));
            }

            ps.addBatch();
            if (counter % GenericConfig.batchRowsSize == 0)
                ps.executeBatch();
            if (counter % GenericConfig.commitRowsSize == 0)
                writeConnection.getConnection().commit();
            counter++;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

    public void start() throws SQLException {
        String updateData = String.format("update %s set %s where %s",
                table.getName(),
                String.join(",", columns.stream().map(c -> c.getName() + "=?").collect(Collectors.toList())),
                String.join(" and ", primaryKeyColumns.stream().map(c -> c.getName() + "=?").collect(Collectors.toList()))
        );
        ps = writeConnection.getConnection().prepareStatement(updateData);
    }

    public void end() throws SQLException {
        ps.executeBatch();
        writeConnection.getConnection().commit();
    }
}
