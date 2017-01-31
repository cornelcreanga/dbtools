package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.Table;

import java.util.List;

public interface ScriptGenerator {

    String generateLoadCommand(Table table, List<Column> columns, String folderName);

    void end(Table table);

}
