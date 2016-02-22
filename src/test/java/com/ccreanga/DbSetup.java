package com.ccreanga;

import java.sql.SQLException;

public interface DbSetup {

    void initialize(String server,String schema,String user,String password) throws Exception;

    void close() throws SQLException;

}
