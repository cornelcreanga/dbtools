package com.ccreanga.jdbc.model;

public class Schema {

    private final String name;

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schema schema = (Schema) o;

        return !(name != null ? !name.equals(schema.name) : schema.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public Schema(String name) {
        this.name = name;
    }
}
