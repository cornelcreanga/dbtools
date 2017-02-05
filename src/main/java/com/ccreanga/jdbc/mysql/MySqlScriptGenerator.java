package com.ccreanga.jdbc.mysql;

import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.jdbc.ScriptGenerator;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.Table;
import com.ccreanga.util.IOUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Types;
import java.util.List;

public class MySqlScriptGenerator implements ScriptGenerator {

    private Writer writer;
    private String folderName;

    public MySqlScriptGenerator(String folderName,boolean override) {
        this.folderName = folderName;

        try {
            writer = new BufferedWriter(new FileWriter(folderName+File.separator + "operations.txt",!override));
        } catch (IOException e) {
            System.out.println("\nException occured, message is " + e.getMessage());
            throw new IOExceptionRuntime(e);
        }
    }


    @Override
    public void startProcessingTable(Table table, List<Column> columns) {
        StringBuilder sb = new StringBuilder("LOAD DATA LOCAL INFILE '" + folderName + File.separator + table.getName() + ".txt'" + " INTO TABLE `" + table.getName() + "` (");

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
        sb.append(";\n");

        try {
            writer.write(sb.toString());
        } catch (IOException e) {
            System.out.println("\nException occured, message is " + e.getMessage());
            throw new IOExceptionRuntime(e);
        }
    }

    @Override
    public void endProcessingTable(Table table) {
    }

    @Override
    public void close() {
        IOUtil.closeSilent(writer);
    }

    private boolean binaryType(Column c) {
        return ((c.getType() == Types.BLOB) || (c.getType() == Types.LONGVARBINARY) || (c.getType() == Types.VARBINARY) || (c.getType() == Types.BINARY));
    }

}
