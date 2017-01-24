package com.ccreanga.usecases.export;

import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.cassandra.CassandraOperations;
import com.ccreanga.cassandra.CassandraScriptGenerator;
import com.ccreanga.cassandra.CassandraWriterConsumer;
import com.ccreanga.jdbc.ScriptGenerator;
import com.ccreanga.jdbc.ScriptGeneratorFactory;
import com.ccreanga.jdbc.*;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.jdbc.model.Table;
import com.ccreanga.util.Wildcard;
import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;

import java.io.*;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CassandraExport {


    private DataAnonymizer anonymizer;

    public CassandraExport() {
    }

    public CassandraExport(DataAnonymizer anonymizer) {
        this.anonymizer = anonymizer;
    }

    public void exportTables(Session session, String keyspace, String tablePattern, String folderName, boolean override) {
        CassandraScriptGenerator generator = new CassandraScriptGenerator();
        File folder = createFolder(folderName);
        File operationsFile = createOperationsFile(folder);

        Collection<TableMetadata> tables;
        tables = session.getCluster().getMetadata().getKeyspace(keyspace).getTables();

        Writer opWriter = null;
        try {
            opWriter = new BufferedWriter(new FileWriter(operationsFile));
        } catch (IOException e) {
            System.out.println("\nException occured, message is " + e.getMessage());
            throw new IOExceptionRuntime(e);
        }

        for (TableMetadata t : tables) {
            if (Wildcard.matches(t.getName(), tablePattern)) {
                System.out.println("\nProcessing table " + t.getName());
                List<ColumnMetadata> columns = t.getColumns();

                File dumpFile = new File(folder.getAbsolutePath() + File.separator + t.getName() + ".txt");
                if (dumpFile.exists()) {
                    if (!override) {
                        System.out.println("skipping table, dump file exists and override is set to false");
                        return;
                    }

                }
                CassandraOperations operations = new CassandraOperations();

                try (CloseableConsumer writerConsumer = new CassandraWriterConsumer(dumpFile,columns)) {

                    List<String> columnNames = columns.stream().map(ColumnMetadata::getName).collect(Collectors.toList());

                    Consumer<List<Object>> consumer = anonymizer == null ?
                            writerConsumer :
                            new AnonymizerConsumer(anonymizer, t.getName(), columnNames).andThen(writerConsumer);

                    operations.processTableRows(session, t, columns, consumer);

                    opWriter.write(generator.generateLoadCommand(t.getName(), columnNames, folderName) + "\n");
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
