package com.ccreanga.usecases.export;

import com.ccreanga.cassandra.CassandraWriterConsumer;
import com.ccreanga.jdbc.Dialect;
import com.ccreanga.usecases.export.mysql.MySqlCSVWriterConsumer;
import com.ccreanga.usecases.export.postgresql.PostgreSqlCSVWriterConsumer;

import java.io.File;
import java.io.IOException;

public class CSVWriterFactory {

    private CSVWriterFactory() {

    }

    public static CloseableConsumer getCSVWriter(Dialect dialect, File file){
        if (dialect == Dialect.MYSQL) {
            return new MySqlCSVWriterConsumer(file);
        } else if (dialect == Dialect.POSTGRESQL) {
            return new PostgreSqlCSVWriterConsumer(file);
        } else
            throw new IllegalArgumentException("unknown dialect:" + dialect);
    }

}
