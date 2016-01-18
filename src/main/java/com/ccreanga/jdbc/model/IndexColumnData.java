package com.ccreanga.jdbc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class IndexColumnData implements Comparable, Serializable {


    private String tableCatalog;
    private String tableSchema;
    private String tableName;
    private boolean nonUnique;
    private String indexQualifier;
    private String indexName;
    private short type;
    private short ordinalPosition;
    private String columnName;
    private String sorterOrd;
    private int cardinality;
    private int pages;
    private String filterCondition;


    public IndexColumnData() {
        super();
        // TODO Auto-generated constructor stub
    }


    /**
     * @return int
     */
    public int getCardinality() {
        return cardinality;
    }

    /**
     * @return String
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @return String
     */
    public String getFilterCondition() {
        return filterCondition;
    }

    /**
     * @return String
     */
    public String getIndexName() {
        return indexName;
    }

    /**
     * @return String
     */
    public String getIndexQualifier() {
        return indexQualifier;
    }

    /**
     * @return boolean
     */
    public boolean isNonUnique() {
        return nonUnique;
    }

    /**
     * @return short
     */
    public short getOrdinalPosition() {
        return ordinalPosition;
    }

    /**
     * @return int
     */
    public int getPages() {
        return pages;
    }

    /**
     * @return String
     */
    public String getSorterOrd() {
        return sorterOrd;
    }

    /**
     * @return String
     */
    public String getTableCatalog() {
        return tableCatalog;
    }

    /**
     * @return String
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @return String
     */
    public String getTableSchema() {
        return tableSchema;
    }

    /**
     * @return short
     */
    public short getType() {
        return type;
    }

    /**
     * Sets the cardinality.
     *
     * @param cardinality The cardinality to set
     */
    public void setCardinality(int cardinality) {
        this.cardinality = cardinality;
    }

    /**
     * Sets the columnName.
     *
     * @param columnName The columnName to set
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * Sets the filterCondition.
     *
     * @param filterCondition The filterCondition to set
     */
    public void setFilterCondition(String filterCondition) {
        this.filterCondition = filterCondition;
    }

    /**
     * Sets the indexName.
     *
     * @param indexName The indexName to set
     */
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    /**
     * Sets the indexQualifier.
     *
     * @param indexQualifier The indexQualifier to set
     */
    public void setIndexQualifier(String indexQualifier) {
        this.indexQualifier = indexQualifier;
    }

    /**
     * Sets the nonUnique.
     *
     * @param nonUnique The nonUnique to set
     */
    public void setNonUnique(boolean nonUnique) {
        this.nonUnique = nonUnique;
    }

    /**
     * Sets the ordinalPosition.
     *
     * @param ordinalPosition The ordinalPosition to set
     */
    public void setOrdinalPosition(short ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    /**
     * Sets the pages.
     *
     * @param pages The pages to set
     */
    public void setPages(int pages) {
        this.pages = pages;
    }

    /**
     * Sets the sorterOrd.
     *
     * @param sorterOrd The sorterOrd to set
     */
    public void setSorterOrd(String sorterOrd) {
        this.sorterOrd = sorterOrd;
    }

    /**
     * Sets the tableCatalog.
     *
     * @param tableCatalog The tableCatalog to set
     */
    public void setTableCatalog(String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    /**
     * Sets the tableName.
     *
     * @param tableName The tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Sets the tableSchema.
     *
     * @param tableSchema The tableSchema to set
     */
    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    /**
     * Sets the type.
     *
     * @param type The type to set
     */
    public void setType(short type) {
        this.type = type;
    }


    public int compareTo(Object o) {
        IndexColumnData ind = (IndexColumnData) o;
        int comp = 0;
        if (ind.getTableName() == null) {
            return 1;
        }
        if (tableName == null) {
            return -1;
        }
        comp = tableName.compareTo(ind.getTableName());
        if (comp != 0) {
            return comp;
        }
        if (ind.getIndexName() == null) {
            return -1;
        }
        if (indexName == null) {
            return -1;
        }

        comp = indexName.compareTo(ind.getIndexName());
        if (comp != 0) {
            return comp;
        }
        if (columnName == null) {
            return -1;
        }
        comp = columnName.compareTo(ind.getColumnName());
        if (comp != 0) {
            return comp;
        }
        return ordinalPosition - ind.getOrdinalPosition();
    }

    public boolean isFromTheSameIndex(IndexColumnData ind) {
        if (!tableName.equals(ind.getTableName())) {
            return false;
        }
        if (!indexName.equals(ind.getIndexName())) {
            return false;
        }
        /*if (!columnName.equals(ind.getColumnName()))
       return false;*/
        return true;
    }

    public String toString() {
        return "IndexColumn{" +
                "cardinality=" + cardinality +
                ", tableCatalog='" + tableCatalog + "'" +
                ", tableSchema='" + tableSchema + "'" +
                ", tableName='" + tableName + "'" +
                ", nonUnique=" + nonUnique +
                ", indexQualifier='" + indexQualifier + "'" +
                ", indexName='" + indexName + "'" +
                ", type=" + type +
                ", ordinalPosition=" + ordinalPosition +
                ", columnName='" + columnName + "'" +
                ", sorterOrd='" + sorterOrd + "'" +
                ", pages=" + pages +
                ", filterCondition='" + filterCondition + "'" +
                "}";
    }
}
