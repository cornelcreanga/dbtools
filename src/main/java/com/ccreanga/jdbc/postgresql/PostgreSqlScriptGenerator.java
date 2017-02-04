package com.ccreanga.jdbc.postgresql;

import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.jdbc.ScriptGenerator;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.Table;
import com.ccreanga.util.IOUtil;

import java.io.*;
import java.util.List;

public class PostgreSqlScriptGenerator implements ScriptGenerator {

    private Writer writer;
    private String folderName;

    public PostgreSqlScriptGenerator(String folderName) {
        this.folderName = folderName;

        try {
            writer = new BufferedWriter(new FileWriter(folderName+File.separator + "operations.txt"));
        } catch (IOException e) {
            System.out.println("\nException occured, message is " + e.getMessage());
            throw new IOExceptionRuntime(e);
        }
    }


    @Override
    public void startProcessingTable(Table table, List<Column> columns) {
        String copy = "COPY " + table.getName() + " FROM '" + folderName + File.separator + table.getName() + ".txt' CSV NULL '\\N';\n";
        try {
            writer.write(copy);
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

}
