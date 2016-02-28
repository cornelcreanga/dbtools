package com.ccreanga.usecases.export;

import com.ccreanga.jdbc.DatabaseException;
import com.ccreanga.jdbc.Operations;
import com.ccreanga.jdbc.OperationsFactory;
import com.ccreanga.jdbc.TableOperations;
import com.ccreanga.jdbc.model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MySqlTablesAnonymizer {

    private DataAnonymizer anonymizer;

    public MySqlTablesAnonymizer(DataAnonymizer anonymizer) {
        this.anonymizer = anonymizer;
    }

    public void anonymizeTables(DbConnection readConnection, DbConnection writeConnection, Schema schema) {
        Set<String> tables = anonymizer.getTablesToAnonymize();

        Operations model = OperationsFactory.createOperations(readConnection.getDialect());

        for (String tableName : tables) {
            Optional<Table> optTable = model.getTable(readConnection, schema.getName(), tableName);
            if (!optTable.isPresent()) {
                System.out.println("Cannot find the table " + tableName + " in schema " + schema.getName());
                continue;
            }
            Table table = optTable.get();

            Set<String> columns = anonymizer.getTableColumnsToAnonymize(tableName);
            List<Column> tableColumns = model.getColumns(readConnection, schema.getName(), tableName);
            List<Key> primaryKeys = model.getTablePrimaryKeys(readConnection, schema.getName(), tableName);
            List<Column> primaryColumns = Table.getTablePrimaryKeyColumns(tableColumns, primaryKeys);

            Set<String> columnNames = tableColumns.stream().map(Column::getName).collect(Collectors.toSet());
            boolean skip = false;
            for (String columnToAnonymize : columns) {
                if (!columnNames.contains(columnToAnonymize)) {
                    System.out.println("table " + tableName + " does not have the column " + columnToAnonymize + "; the anonymization on this table was skipped");
                    skip = true;
                    break;
                }
            }
            if (skip)
                continue;
            List<Column> filteredColumns = tableColumns.stream().filter(c -> columns.contains(c.getName())).collect(Collectors.toList());

            List<Column> columnsToRead = new ArrayList<>(filteredColumns);
            columnsToRead.addAll(primaryColumns);
            System.out.println("\nProcessing table " + table.getName());
            TableOperations tableOperations = new TableOperations();
            MySQLAnonymizerConsumer consumer = new MySQLAnonymizerConsumer(anonymizer, writeConnection, table, filteredColumns, primaryColumns);
            try {
                consumer.start();
                tableOperations.processTableRows(readConnection, table, columnsToRead, consumer);
                consumer.end();
                consumer.close();
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }

        }
    }

}
