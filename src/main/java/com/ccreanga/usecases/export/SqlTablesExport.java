package com.ccreanga.usecases.export;

import com.ccreanga.jdbc.GenericException;
import com.ccreanga.jdbc.Operations;
import com.ccreanga.jdbc.OperationsFactory;
import com.ccreanga.jdbc.ScriptGenerator;
import com.ccreanga.jdbc.ScriptGeneratorFactory;
import com.ccreanga.jdbc.TableOperations;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.jdbc.model.Table;
import com.ccreanga.util.Wildcard;

import java.io.*;
import java.sql.Types;
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
        Operations model = OperationsFactory.createOperations(connection.getDialect());
        ScriptGenerator generator = ScriptGeneratorFactory.createScriptGenerator(connection.getDialect());
        File folder = createFolder(folderName);
        File operations = createOperationsFile(folder);

        try (Writer opWriter = new BufferedWriter(new FileWriter(operations))) {
            List<Table> tables = model.getAllTables(connection, schema.getName());
            tables.stream().filter(t -> Wildcard.matches(t.getName(), tablePattern)).forEach(t -> {

                        System.out.println("\nProcessing table " + t.getName());
                        List<Column> columns = model.getColumns(connection, schema.getName(), t.getName());
                        File dumpFile = new File(folder.getAbsolutePath() + File.separator + t.getName() + ".txt");
                        if (dumpFile.exists()) {
                            if (!override)
                                return;
                        }
                        TableOperations tableOperations = new TableOperations();

                        try (CloseableConsumer writerConsumer = CSVWriterFactory.getCSVWriter(connection.getDialect(),dumpFile)) {
                            Consumer<List<Object>> consumer = anonymizer == null ?
                                    writerConsumer :
                                    new AnonymizerConsumer(anonymizer, t, columns).andThen(writerConsumer);

                            tableOperations.processTableRows(connection, t, columns, consumer);
                        } catch (IOException e) {
                            if (!dumpFile.delete())
                                System.out.println("exception occured, trying to clean the dump file but failed");
                            throw new GenericException(e);
                        }
                        try {
                            opWriter.write(generator.generateLoadCommand(t, columns, folderName) + "\n");
                        } catch (Exception e) {
                            throw new GenericException(e);
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
