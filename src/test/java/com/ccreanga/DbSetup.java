package com.ccreanga;

import java.sql.Connection;
import java.sql.SQLException;

public interface DbSetup {

    void initialize(Connection connection) throws Exception;

    void close() throws SQLException;

}
