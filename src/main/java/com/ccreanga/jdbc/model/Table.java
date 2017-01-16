package com.ccreanga.jdbc.model;


import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class Table {
    private final String schema;
    private final String name;
    private final String tableType;
    private final String comments;

    public static List<Column> getTablePrimaryKeyColumns(List<Column> columns, List<Key> primaryKeys) {
        return columns.stream().filter(c -> {
            for (Key key : primaryKeys) {
                if (key.getColumn().equals(c.getName()))
                    return true;
            }
            return false;
        }).collect(Collectors.toList());
    }
}
