package com.ccreanga.jdbc.model;


public class Relation {
    private final Key primaryKey;
    private final Key foreignKey;
    private final int updateRule;
    private final int deleteRule;
    private final int deferrability;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Relation relation = (Relation) o;

        if (updateRule != relation.updateRule) return false;
        if (deleteRule != relation.deleteRule) return false;
        if (deferrability != relation.deferrability) return false;
        if (primaryKey != null ? !primaryKey.equals(relation.primaryKey) : relation.primaryKey != null) return false;
        return !(foreignKey != null ? !foreignKey.equals(relation.foreignKey) : relation.foreignKey != null);

    }

    @Override
    public int hashCode() {
        int result = primaryKey != null ? primaryKey.hashCode() : 0;
        result = 31 * result + (foreignKey != null ? foreignKey.hashCode() : 0);
        result = 31 * result + updateRule;
        result = 31 * result + deleteRule;
        result = 31 * result + deferrability;
        return result;
    }

    public Key getPrimaryKey() {
        return primaryKey;
    }

    public Key getForeignKey() {
        return foreignKey;
    }

    public int getUpdateRule() {
        return updateRule;
    }

    public int getDeleteRule() {
        return deleteRule;
    }

    public int getDeferrability() {
        return deferrability;
    }

    public Relation(Key primaryKey, Key foreignKey, int updateRule, int deleteRule, int deferrability) {
        this.primaryKey = primaryKey;
        this.foreignKey = foreignKey;
        this.updateRule = updateRule;
        this.deleteRule = deleteRule;
        this.deferrability = deferrability;
    }
}
