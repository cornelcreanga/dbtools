package com.ccreanga.jdbc.model;


import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class Table {
    private final String schema;
    private final String name;
    private final String tableType;
    private final String comments;

}
