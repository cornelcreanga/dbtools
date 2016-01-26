package com.ccreanga.usecases.export;

import com.ccreanga.jdbc.BasicModelOperations;
import com.ccreanga.jdbc.GenericException;
import com.ccreanga.jdbc.TableOperations;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.jdbc.model.Table;
import com.mysql.fabric.xmlrpc.base.Data;

import java.io.*;
import java.sql.Types;
import java.util.List;

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

        final Writer opWriter;
        try {
            opWriter = new BufferedWriter(new FileWriter(operations));
        } catch (IOException e) {
            throw new GenericException(e);
        }

        List<Table> tables = model.getTables(connection, schema.getName());
        System.out.println("Found "+tables.size()+" tables.");
        tables.stream().filter(t -> t.getName().matches(tablePattern)).forEach(t -> {
                    System.out.println("\nProcessing table:"+t.getName());
                    File dumpFile = new File(folder.getAbsolutePath() + File.separator + t.getName() + ".txt");
                    if (dumpFile.exists()) {
                        if (!override)
                            return;
                        System.out.println("Overriding file:" + dumpFile.getName());
                    }
                    TableOperations tableOperations = new TableOperations();

                    MySQLCSVWriterConsumer mySQLCSVWriter = null;
                    AnonymizerConsumer anonymizerConsumer = new AnonymizerConsumer(anonymizer,t);
                    try {

                        mySQLCSVWriter = new MySQLCSVWriterConsumer(dumpFile);
                        tableOperations.processTableRows(connection, t, anonymizerConsumer.andThen(mySQLCSVWriter));
                    } catch (IOException e) {
                        if (!dumpFile.delete())
                            System.out.println("exception occured, trying to clean the dump file but failed");
                        throw new GenericException(e);
                    } finally {
                        if (mySQLCSVWriter != null)
                            mySQLCSVWriter.close();
                    }
                    try {
                        opWriter.write(loadInline(t)+"\n");
                    } catch (Exception e) {
                        throw new GenericException(e);
                    }
                }

        );
        try {
            opWriter.close();
        } catch (IOException e) {
            //ignore
        }

    }


    private String loadInline(Table table) {
        StringBuilder sb = new StringBuilder("LOAD DATA INFILE " + table.getName() +".txt"+ " INTO TABLE `"+table.getName()+"` (");

        boolean found = false;
        List<Column> columns = table.getColumns();
        for (Column c : columns) {
            if (binaryType(c)) {
                sb.append("@");
                found = true;
            }
            sb.append(c.getName()).append(",");
        }
        sb.deleteCharAt(sb.length()-1);

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
