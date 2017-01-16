package com.ccreanga.jdbc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Key implements Serializable {

    private final String schema;
    private final String table;
    private final String column;

    private final String seqName;
    private final String name;

}
