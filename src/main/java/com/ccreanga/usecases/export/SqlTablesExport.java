package com.ccreanga.usecases.export;

import com.ccreanga.jdbc.*;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.jdbc.model.Table;
import com.ccreanga.util.Wildcard;

import java.io.*;
import java.util.List;
import java.util.function.Consumer;

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

        try (Writer opWriter = new BufferedWriter(new FileWriter(operationsFile))) {
            List<Table> tables;
            try {
                tables = operations.getAllTables(connection, schema.getName());
            } catch (DatabaseException d) {
                System.out.println("Exception occured during metadata read operations, message is " + d.getMessage());
                throw d;
            }
            tables.stream().filter(t -> Wildcard.matches(t.getName(), tablePattern)).forEach(t -> {

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
                            Consumer<List<Object>> consumer = anonymizer == null ?
                                    writerConsumer :
                                    new AnonymizerConsumer(anonymizer, t, columns).andThen(writerConsumer);

                            tableOperations.processTableRows(connection, t, columns, consumer);
                        } catch (IOException | DatabaseException e) {
                            System.out.println("\nException occured, message is " + e.getMessage());
                            if (connection.isClosed())
                                throw new DatabaseException(e);
                        }
                        try {
                            opWriter.write(generator.generateLoadCommand(t, columns, folderName) + "\n");
                        } catch (Exception e) {
                            System.out.println("Can't generate the operations file, message is " + e.getMessage());
                        }
                    }


            );
            System.out.println();
        } catch (IOException e) {
            throw new GenericException(e);
        }

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
                throw new GenericException("cannot create " + folder);
        }
        return folder;
    }

}
