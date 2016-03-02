package com.ccreanga;

public class DataSource {
    private String user;
    private String password;
    private String schema;
    private String server;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataSource that = (DataSource) o;

        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (schema != null ? !schema.equals(that.schema) : that.schema != null) return false;
        return !(server != null ? !server.equals(that.server) : that.server != null);

    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (schema != null ? schema.hashCode() : 0);
        result = 31 * result + (server != null ? server.hashCode() : 0);
        return result;
    }
}