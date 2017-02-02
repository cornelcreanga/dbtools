package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.DbConnection;

import java.sql.ResultSet;
import java.sql.Statement;

public class JdbcUtil {

    public static String schema(String schema,Dialect dialect){
        if (dialect==Dialect.ORACLE)
            return schema.toUpperCase();
        return "%";
    }

    public static String catalog(String schema, Dialect dialect){
        if (dialect==Dialect.ORACLE)
            return null;
        return schema;
    }

    public static Object singleResultQuery(DbConnection connection, String query) {
        try (Statement st = connection.getConnection().createStatement()) {
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                return rs.getObject(1);
            }
            return null;
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }

}
