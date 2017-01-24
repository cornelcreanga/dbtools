package com.ccreanga.usecases.export;

import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.jdbc.ScriptGenerator;
import com.ccreanga.jdbc.ScriptGeneratorFactory;
import com.ccreanga.jdbc.*;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.jdbc.model.Table;
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
        Operations operations = OperationsFactory.createOperations(connection.getDialect());
        ScriptGenerator generator = ScriptGeneratorFactory.createScriptGenerator(connection.getDialect());
        File folder = createFolder(folderName);
        File operationsFile = createOperationsFile(folder);

        List<Table> tables;
        try {
            tables = operations.getAllTables(connection, schema.getName());
        } catch (DatabaseException d) {
            System.out.println("Exception occured during metadata read operations, message is " + d.getMessage());
            throw d;
        }

        Writer opWriter = null;
        try {
            opWriter = new BufferedWriter(new FileWriter(operationsFile));
        } catch (IOException e) {
            System.out.println("\nException occured, message is " + e.getMessage());
            throw new IOExceptionRuntime(e);
        }

        for (Table t : tables) {
            if (Wildcard.matches(t.getName(), tablePattern)) {
                System.out.println("\nProcessing table " + t.getName());
                List<Column> columns;
                try {
                    columns = operations.getColumns(connection, schema.getName(), t.getName());
                } catch (DatabaseException d) {
                    System.out.println("Exception occured during metadata read operations, message is " + d.getMessage());
                    throw d;
                }

                File dumpFile = new File(folder.getAbsolutePath() + File.separator + t.getName() + ".txt");
                if (dumpFile.exists()) {
                    if (!override) {
                        System.out.println("skipping table, dump file exists and override is set to false");
                        return;
                    }

                }
                TableOperations tableOperations = new TableOperations();

                try (CloseableConsumer writerConsumer = CSVWriterFactory.getCSVWriter(connection.getDialect(), dumpFile)) {
                    List<String> columnNames = columns.stream().map(Column::getName).collect(Collectors.toList());
                    Consumer<List<Object>> consumer = anonymizer == null ?
                            writerConsumer :
                            new AnonymizerConsumer(anonymizer, t.getName(), columnNames).andThen(writerConsumer);

                    tableOperations.processTableRows(connection, t, columns, consumer);

                    opWriter.write(generator.generateLoadCommand(t, columns, folderName) + "\n");
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
        try {
            opWriter.close();
        } catch (IOException e) {
        }
        System.out.println();

    }

    private File createOperationsFile(File folder) {
        File operations = new File(folder.getAbsolutePath() + File.separator + "operations.txt");
        if (operations.exists()) {
            System.out.println("overriding file:" + operations.getName());
        }
        return operations;
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
