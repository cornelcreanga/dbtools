package com.ccreanga.usecases.info;

import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.jdbc.*;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.jdbc.model.Table;
import com.ccreanga.util.Wildcard;

import java.util.List;
import java.util.stream.Collectors;

public class SqlTablesInfo {

    public void info(DbConnection connection, Schema schema, String tablePattern) {
        DbOperations dbOperations = OperationsFactory.createDbOperations(connection.getDialect());
        TableOperations tableOperations = OperationsFactory.createTableOperations(connection.getDialect());

        List<Table> tables;
        try {
            tables = dbOperations.getAllTables(connection, schema.getName());
        } catch (DatabaseException d) {
            System.out.println("Can't obtain table names, message is " + d.getMessage());
            throw d;
        }

        for (Table table : tables) {
            if (Wildcard.matches(table.getName(), tablePattern)) {
                System.out.println("\nProcessing table " + table.getName());
                List<Column> columns;
                try {
                    columns = tableOperations.getColumns(connection, schema.getName(), table.getName());
                } catch (DatabaseException d) {
                    System.out.println("Exception occured during metadata read dbOperations, message is " + d.getMessage());
                    throw d;
                }
                CheckEmpty consumer = new CheckEmpty(columns.size());

                List<String> columnNames = columns.stream().map(Column::getName).collect(Collectors.toList());
                try{
                    tableOperations.processTableRows(connection, table, columns,consumer);
                } catch (DatabaseException e) {
                    System.out.println("\nException occured, message is " + e.getMessage());
                    if (connection.isClosed())
                        throw new DatabaseException(e);
                } catch (IOExceptionRuntime e) {
                    System.out.println("\nException occured, message is " + e.getMessage());
                    throw e;
                }
                int[] c = consumer.getNonEmpty();
                for (int i = 0; i < columnNames.size(); i++) {
                    if (c[i]==0)
                        System.out.println(columnNames.get(i));
                }
            }
        }
        System.out.println();

    }

}
