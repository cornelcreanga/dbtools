package com.ccreanga.jdbc.oracle;

import com.ccreanga.jdbc.ScriptGenerator;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.Table;

import java.util.List;

public class OracleScriptGenerator implements ScriptGenerator {
    @Override
    public String generateLoadCommand(Table table, List<Column> columns, String folderName) {
        return null;
    }
}
