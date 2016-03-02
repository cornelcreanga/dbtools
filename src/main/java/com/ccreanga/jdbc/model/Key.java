package com.ccreanga.jdbc.model;


public class Key {

    private final String schema;
    private final String table;
    private final String column;

    private final String seqName;
    private final String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key = (Key) o;

        if (schema != null ? !schema.equals(key.schema) : key.schema != null) return false;
        if (table != null ? !table.equals(key.table) : key.table != null) return false;
        if (column != null ? !column.equals(key.column) : key.column != null) return false;
        if (seqName != null ? !seqName.equals(key.seqName) : key.seqName != null) return false;
        return !(name != null ? !name.equals(key.name) : key.name != null);

    }

    @Override
    public int hashCode() {
        int result = schema != null ? schema.hashCode() : 0;
        result = 31 * result + (table != null ? table.hashCode() : 0);
        result = 31 * result + (column != null ? column.hashCode() : 0);
        result = 31 * result + (seqName != null ? seqName.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public String getColumn() {
        return column;
    }

    public String getSeqName() {
        return seqName;
    }

    public String getName() {
        return name;
    }

    public Key(String schema, String table, String column, String seqName, String name) {
        this.schema = schema;
        this.table = table;
        this.column = column;
        this.seqName = seqName;
        this.name = name;
    }
}
