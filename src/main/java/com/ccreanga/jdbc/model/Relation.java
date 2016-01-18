package com.ccreanga.jdbc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Relation implements Serializable {
    private final Key primaryKey;
    private final Key foreignKey;
    private final int updateRule;
    private final int deleteRule;
    private final int deferrability;

}
