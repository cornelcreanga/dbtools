package com.ccreanga.usecases.process;

import com.ccreanga.jdbc.*;
import com.ccreanga.jdbc.model.*;
import com.ccreanga.usecases.export.DataAnonymizer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class SqlTablesAnonymizer {

    private DataAnonymizer anonymizer;

    public SqlTablesAnonymizer(DataAnonymizer anonymizer) {
        this.anonymizer = anonymizer;
    }

    public void anonymizeTables(DbConnection readConnection, DbConnection writeConnection, Schema schema) {
        Set<String> tables = anonymizer.getTablesToAnonymize();
        DbOperations dbOperations = OperationsFactory.createDbOperations(readConnection.getDialect());
        TableOperations tableOperations = OperationsFactory.createTableOperations(readConnection.getDialect());

        for (String tableName : tables) {
            Optional<Table> optTable = dbOperations.getTable(readConnection, schema.getName(), tableName);
            if (!optTable.isPresent()) {
                System.out.printf("Cannot find the table %s in schema %s\n", tableName, schema.getName());
                continue;
            }
            Table table = optTable.get();

            Set<String> columns = anonymizer.getTableColumnsToAnonymize(tableName);
            List<Column> tableColumns = tableOperations.getColumns(readConnection, schema.getName(), tableName);
            List<Key> primaryKeys = tableOperations.getTablePrimaryKeys(readConnection, schema.getName(), tableName);
            List<Column> primaryColumns = Table.getTablePrimaryKeyColumns(tableColumns, primaryKeys);
            if (primaryColumns.size() == 0) {
                System.out.printf("table %s does not have any primary keys - it cannot be updated\n", tableName);
                break;
            }


            Set<String> columnNames = tableColumns.stream().map(Column::getName).collect(Collectors.toSet());
            boolean skip = false;
            for (String columnToAnonymize : columns) {
                if (!columnNames.contains(columnToAnonymize)) {
                    System.out.printf("table %s  does not have the column %s; the anonymization on this table was skipped\n", tableName, columnToAnonymize);
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
            SqlAnonymizerConsumer consumer = new SqlAnonymizerConsumer(anonymizer, writeConnection, table, filteredColumns, primaryColumns);
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
