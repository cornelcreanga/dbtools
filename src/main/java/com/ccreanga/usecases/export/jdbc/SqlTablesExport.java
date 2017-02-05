package com.ccreanga.usecases.export.jdbc;

import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.jdbc.*;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.jdbc.model.Table;
import com.ccreanga.usecases.export.AnonymizerConsumer;
import com.ccreanga.usecases.export.DataAnonymizer;
import com.ccreanga.util.Wildcard;

import java.io.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SqlTablesExport {

    private DataAnonymizer anonymizer;

    public SqlTablesExport() {
    }

    public SqlTablesExport(DataAnonymizer anonymizer) {
        this.anonymizer = anonymizer;
    }

    public void exportTables(DbConnection connection, Schema schema, String tablePattern, String folderName, boolean override) {
        DbOperations dbOperations = OperationsFactory.createDbOperations(connection.getDialect());
        TableOperations tableOperations = OperationsFactory.createTableOperations(connection.getDialect());
        ScriptGenerator generator = ScriptGeneratorFactory.createScriptGenerator(connection.getDialect(),folderName,override);
        File folder = createFolder(folderName);

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

                File dumpFile = new File(folder.getAbsolutePath() + File.separator + table.getName() + ".txt");
                if (dumpFile.exists()) {
                    if (!override) {
                        System.out.println("skipping table, dump file exists and override is set to false");
                        return;
                    }

                }
                List<String> columnNames = columns.stream().map(Column::getName).collect(Collectors.toList());
                try (CloseableConsumer writerConsumer = CSVWriterFactory.getCSVWriter(connection.getDialect(), dumpFile,table.getName(),columnNames)) {

                    Consumer<List<Object>> consumer = anonymizer == null ?
                            writerConsumer :
                            new AnonymizerConsumer(anonymizer, table.getName(), columnNames).andThen(writerConsumer);

                    generator.startProcessingTable(table, columns);

                    tableOperations.processTableRows(connection, table, columns, consumer);

                    generator.endProcessingTable(table);
                } catch (DatabaseException e) {
                    System.out.println("\nException occured, message is " + e.getMessage());
                    if (connection.isClosed())
                        throw new DatabaseException(e);
                } catch (IOExceptionRuntime e) {
                    System.out.println("\nException occured, message is " + e.getMessage());
                    throw e;
                } catch (IOException e) {
                    throw new IOExceptionRuntime(e);
                }
            }
        }
        generator.close();
        System.out.println();

    }

    private File createFolder(String folderName) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            if (!folder.mkdirs())
                throw new IOExceptionRuntime("cannot create " + folder);
        }
        return folder;
    }

}
