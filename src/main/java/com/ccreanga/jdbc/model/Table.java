package com.ccreanga.jdbc.model;


import java.util.List;
import java.util.stream.Collectors;

public class Table {
    private final String schema;
    private final String name;
    private final String tableType;
    private final String comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        if (schema != null ? !schema.equals(table.schema) : table.schema != null) return false;
        if (name != null ? !name.equals(table.name) : table.name != null) return false;
        if (tableType != null ? !tableType.equals(table.tableType) : table.tableType != null) return false;
        return !(comments != null ? !comments.equals(table.comments) : table.comments != null);

    }

    @Override
    public int hashCode() {
        int result = schema != null ? schema.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (tableType != null ? tableType.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        return result;
    }

    public String getSchema() {
        return schema;
    }

    public String getName() {
        return name;
    }

    public String getTableType() {
        return tableType;
    }

    public String getComments() {
        return comments;
    }

    public Table(String schema, String name, String tableType, String comments) {
        this.schema = schema;
        this.name = name;
        this.tableType = tableType;
        this.comments = comments;
    }

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
