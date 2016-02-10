package com.ccreanga.usecases.export;

import com.ccreanga.jdbc.BasicModelOperations;
import com.ccreanga.jdbc.GenericException;
import com.ccreanga.jdbc.TableOperations;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.jdbc.model.Table;

import java.io.*;
import java.sql.Types;
import java.util.List;
import java.util.function.Consumer;

public class MySqlTablesExport {

    private DataAnonymizer anonymizer;

    public MySqlTablesExport() {
    }

    public MySqlTablesExport(DataAnonymizer anonymizer) {
        this.anonymizer = anonymizer;
    }

    public void exportTables(DbConnection connection, Schema schema, String tablePattern, String folderName, boolean override) {
        BasicModelOperations model = new BasicModelOperations();
        File folder = createFolder(folderName);
        File operations = createOperationsFile(folder);

        try (Writer opWriter = new BufferedWriter(new FileWriter(operations))) {
            List<Table> tables = model.getTables(connection, schema.getName());
            tables.stream().filter(t -> t.getName().matches(tablePattern)).forEach(t -> {

                System.out.println("\nProcessing table:" + t.getName());
                List<Column> columns = model.getColumns(connection,schema.getName(),t.getName());
                        File dumpFile = new File(folder.getAbsolutePath() + File.separator + t.getName() + ".txt");
                        if (dumpFile.exists()) {
                            if (!override)
                                return;
                        }
                        TableOperations tableOperations = new TableOperations();

                        try (MySQLCSVWriterConsumer mySQLCSVWriter = new MySQLCSVWriterConsumer(dumpFile)) {
                            Consumer<List<Object>> consumer = anonymizer == null ?
                                    mySQLCSVWriter :
                                    new AnonymizerConsumer(anonymizer, t, columns).andThen(mySQLCSVWriter);

                            tableOperations.processTableRows(connection, t,columns, consumer);
                        } catch (IOException e) {
                            if (!dumpFile.delete())
                                System.out.println("exception occured, trying to clean the dump file but failed");
                            throw new GenericException(e);
                        }
                        try {
                            opWriter.write(loadInline(t,columns,folderName) + "\n");
                        } catch (Exception e) {
                            throw new GenericException(e);
                        }
                    }

            );
        } catch (IOException e) {
            throw new GenericException(e);
        }

    }


    private String loadInline(Table table,List<Column> columns,String folderName) {
        StringBuilder sb = new StringBuilder("LOAD DATA LOCAL INFILE '" + folderName + File.separator+table.getName() + ".txt'" + " INTO TABLE `" + table.getName() + "` (");

        boolean found = false;
        for (Column c : columns) {
            if (binaryType(c)) {
                sb.append("@");
                found = true;
            }
            sb.append(c.getName()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        sb.append(")");
        if (found) {
            sb.append(" SET ");

            for (Column c : columns) {
                if (binaryType(c)) {
                    sb.append(c.getName()).append("=UNHEX(@").append(c.getName()).append("),");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(";");

        return sb.toString();
    }

    private boolean binaryType(Column c) {
        return ((c.getType() == Types.BLOB) || (c.getType() == Types.LONGVARBINARY) || (c.getType() == Types.VARBINARY) || (c.getType() == Types.BINARY));
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
