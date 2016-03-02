package com.ccreanga.jdbc.model;

import java.util.ArrayList;

public class Index {

    private final Schema schema;
    private final Table table;
    private final String name;
    private final boolean nonUnique;
    private final int cardinality;
    private final int pages;
    private final String qualifier;
    private final short type;
    private final String filterCondition;

    private ArrayList indexColumns = new ArrayList(1);

    public Index(Schema schema, Table table, String name, boolean nonUnique, int cardinality, int pages, String qualifier, short type, String filterCondition) {
        this.schema = schema;
        this.table = table;
        this.name = name;
        this.nonUnique = nonUnique;
        this.cardinality = cardinality;
        this.pages = pages;
        this.qualifier = qualifier;
        this.type = type;
        this.filterCondition = filterCondition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Index index = (Index) o;

        if (nonUnique != index.nonUnique) return false;
        if (cardinality != index.cardinality) return false;
        if (pages != index.pages) return false;
        if (type != index.type) return false;
        if (schema != null ? !schema.equals(index.schema) : index.schema != null) return false;
        if (table != null ? !table.equals(index.table) : index.table != null) return false;
        if (name != null ? !name.equals(index.name) : index.name != null) return false;
        if (qualifier != null ? !qualifier.equals(index.qualifier) : index.qualifier != null) return false;
        if (filterCondition != null ? !filterCondition.equals(index.filterCondition) : index.filterCondition != null)
            return false;
        return !(indexColumns != null ? !indexColumns.equals(index.indexColumns) : index.indexColumns != null);

    }

    @Override
    public int hashCode() {
        int result = schema != null ? schema.hashCode() : 0;
        result = 31 * result + (table != null ? table.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (nonUnique ? 1 : 0);
        result = 31 * result + cardinality;
        result = 31 * result + pages;
        result = 31 * result + (qualifier != null ? qualifier.hashCode() : 0);
        result = 31 * result + (int) type;
        result = 31 * result + (filterCondition != null ? filterCondition.hashCode() : 0);
        result = 31 * result + (indexColumns != null ? indexColumns.hashCode() : 0);
        return result;
    }

    public Schema getSchema() {
        return schema;
    }

    public Table getTable() {
        return table;
    }

    public String getName() {
        return name;
    }

    public boolean isNonUnique() {
        return nonUnique;
    }

    public int getCardinality() {
        return cardinality;
    }

    public int getPages() {
        return pages;
    }

    public String getQualifier() {
        return qualifier;
    }

    public short getType() {
        return type;
    }

    public String getFilterCondition() {
        return filterCondition;
    }

    public ArrayList getIndexColumns() {
        return indexColumns;
    }

    public void setIndexColumns(ArrayList indexColumns) {
        this.indexColumns = indexColumns;
    }
}
