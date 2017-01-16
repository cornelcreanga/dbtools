package com.ccreanga.jdbc.model;

import lombok.Data;

@Data
public class IndexColumn {


    private short ordinalPosition;
    private Column column;
    private String sorterOrd;

    public IndexColumn(Column column, short ordinalPosition, String sorterOrd) {
        this.column = column;
        this.ordinalPosition = ordinalPosition;
        this.sorterOrd = sorterOrd;
    }


}
