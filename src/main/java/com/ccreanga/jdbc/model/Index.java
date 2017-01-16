package com.ccreanga.jdbc.model;

import lombok.Data;

import java.util.ArrayList;

@Data
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


}
