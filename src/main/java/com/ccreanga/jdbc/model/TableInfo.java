package com.ccreanga.jdbc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TableInfo implements Serializable {
    protected String tableSchema;
    protected String tableName;
    protected String tableType;
    protected String comments;

}
