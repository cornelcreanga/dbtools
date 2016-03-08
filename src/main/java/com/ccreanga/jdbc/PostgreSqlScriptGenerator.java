package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.Table;

import java.io.File;
import java.util.List;

public class PostgreSqlScriptGenerator implements ScriptGenerator{
    @Override
    public String generateLoadCommand(Table table, List<Column> columns, String folderName) {
        return "COPY "+table.getName()+" FROM '"+folderName+ File.separator + table.getName() + ".txt' CSV NULL '\\N';";
    }
}