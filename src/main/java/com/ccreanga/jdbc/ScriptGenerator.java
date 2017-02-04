package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.Table;

import java.util.List;

public interface ScriptGenerator {

    void startProcessingTable(Table table, List<Column> columns);

    void endProcessingTable(Table table);

    void close();

}
