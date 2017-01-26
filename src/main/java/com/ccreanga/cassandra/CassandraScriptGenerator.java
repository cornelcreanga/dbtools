package com.ccreanga.cassandra;

import java.util.List;

public class CassandraScriptGenerator {
    public String generateLoadCommand(String table, List<String> columns, String folderName) {
        return "COPY " + table + " FROM (" + table + ")";
    }
}
