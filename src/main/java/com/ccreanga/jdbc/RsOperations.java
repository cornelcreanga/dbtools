package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.DbConnection;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;

public interface RsOperations {

    public Object readValue(ResultSet rs, int pos, int type);

    /**
     * Immediately closes the result set (without consuming all the remaining rows). Depending on the database server
     * any subsequent call might fail so after closing the result set this method will also close the database connection.
     * When to use it - if some fatal error occurs during a very large result set processing it does not make sense to wait until all the result
     * set is drained from server (as currently implemented in the MYSQL jdbc connector)
     * For the moment only MySQL forces you to read thw whole rs
     *
     * @param connection
     * @param rs
     */
    void forceDiscardResultSetAndCloseConnection(DbConnection connection, ResultSet rs);
}
