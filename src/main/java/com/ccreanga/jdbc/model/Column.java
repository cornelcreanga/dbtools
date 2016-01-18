package com.ccreanga.jdbc.model;

import lombok.Data;

import java.io.Serializable;

@Data
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

}
