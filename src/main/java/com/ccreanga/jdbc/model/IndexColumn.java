package com.ccreanga.jdbc.model;


public class IndexColumn {


    private final short ordinalPosition;
    private final Column column;
    private final String sorterOrd;

    public IndexColumn(Column column, short ordinalPosition, String sorterOrd) {
        this.column = column;
        this.ordinalPosition = ordinalPosition;
        this.sorterOrd = sorterOrd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexColumn that = (IndexColumn) o;

        if (ordinalPosition != that.ordinalPosition) return false;
        if (column != null ? !column.equals(that.column) : that.column != null) return false;
        return !(sorterOrd != null ? !sorterOrd.equals(that.sorterOrd) : that.sorterOrd != null);

    }

    @Override
    public int hashCode() {
        int result = (int) ordinalPosition;
        result = 31 * result + (column != null ? column.hashCode() : 0);
        result = 31 * result + (sorterOrd != null ? sorterOrd.hashCode() : 0);
        return result;
    }

    public short getOrdinalPosition() {
        return ordinalPosition;
    }

    public Column getColumn() {
        return column;
    }

    public String getSorterOrd() {
        return sorterOrd;
    }
}
