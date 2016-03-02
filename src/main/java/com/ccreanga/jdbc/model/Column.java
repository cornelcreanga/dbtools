package com.ccreanga.jdbc.model;


import java.io.Serializable;


public class Column implements Serializable {

    private final String schema;
    private final String table;

    private final String name;
    private final int type;//from java.sql.Types
    private final String typeName;
    private final int size;//(or precisison for decimal types)
    private final int decimalDigits;
    private final int radix;
    private final int allowNullValue;
    private final String remarks;
    private final String defaultValue;
    private final int maxNoBytes;//for char
    private final int position;//index in table
    private final String isNullable;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        if (type != column.type) return false;
        if (size != column.size) return false;
        if (decimalDigits != column.decimalDigits) return false;
        if (radix != column.radix) return false;
        if (allowNullValue != column.allowNullValue) return false;
        if (maxNoBytes != column.maxNoBytes) return false;
        if (position != column.position) return false;
        if (schema != null ? !schema.equals(column.schema) : column.schema != null) return false;
        if (table != null ? !table.equals(column.table) : column.table != null) return false;
        if (name != null ? !name.equals(column.name) : column.name != null) return false;
        if (typeName != null ? !typeName.equals(column.typeName) : column.typeName != null) return false;
        if (remarks != null ? !remarks.equals(column.remarks) : column.remarks != null) return false;
        if (defaultValue != null ? !defaultValue.equals(column.defaultValue) : column.defaultValue != null)
            return false;
        return !(isNullable != null ? !isNullable.equals(column.isNullable) : column.isNullable != null);

    }

    @Override
    public int hashCode() {
        int result = schema != null ? schema.hashCode() : 0;
        result = 31 * result + (table != null ? table.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + size;
        result = 31 * result + decimalDigits;
        result = 31 * result + radix;
        result = 31 * result + allowNullValue;
        result = 31 * result + (remarks != null ? remarks.hashCode() : 0);
        result = 31 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
        result = 31 * result + maxNoBytes;
        result = 31 * result + position;
        result = 31 * result + (isNullable != null ? isNullable.hashCode() : 0);
        return result;
    }

    public Column(String schema, String table, String name, int type, String typeName, int size, int decimalDigits, int radix, int allowNullValue, String remarks, String defaultValue, int maxNoBytes, int position, String isNullable) {
        this.schema = schema;
        this.table = table;
        this.name = name;
        this.type = type;
        this.typeName = typeName;
        this.size = size;
        this.decimalDigits = decimalDigits;
        this.radix = radix;
        this.allowNullValue = allowNullValue;
        this.remarks = remarks;
        this.defaultValue = defaultValue;
        this.maxNoBytes = maxNoBytes;
        this.position = position;
        this.isNullable = isNullable;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getSize() {
        return size;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public int getRadix() {
        return radix;
    }

    public int getAllowNullValue() {
        return allowNullValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public int getMaxNoBytes() {
        return maxNoBytes;
    }

    public int getPosition() {
        return position;
    }

    public String getIsNullable() {
        return isNullable;
    }
}
