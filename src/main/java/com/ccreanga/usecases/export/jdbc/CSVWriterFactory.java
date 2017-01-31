package com.ccreanga.usecases.export.jdbc;

import com.ccreanga.jdbc.Dialect;
import com.ccreanga.usecases.export.jdbc.mysql.MySqlCSVWriterConsumer;
import com.ccreanga.usecases.export.jdbc.oracle.OracleCSVWriterConsumer;
import com.ccreanga.usecases.export.jdbc.postgresql.PostgreSqlCSVWriterConsumer;

import java.io.File;
import java.util.List;

public class CSVWriterFactory {

    private CSVWriterFactory() {

    }

    public static CloseableConsumer getCSVWriter(Dialect dialect, File file,String table,List<String> columns) {
        if (dialect == Dialect.MYSQL) {
            return new MySqlCSVWriterConsumer(file);
        } else if (dialect == Dialect.POSTGRESQL) {
            return new PostgreSqlCSVWriterConsumer(file);
        }else if (dialect == Dialect.ORACLE){
            return new OracleCSVWriterConsumer(file, table,columns);
        } else
            throw new IllegalArgumentException("unknown dialect:" + dialect);
    }

}
