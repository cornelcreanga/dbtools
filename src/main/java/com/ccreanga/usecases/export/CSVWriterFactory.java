package com.ccreanga.usecases.export;

import com.ccreanga.jdbc.Dialect;
import com.ccreanga.usecases.export.mysql.MySqlCSVWriterConsumer;
import com.ccreanga.usecases.export.postgresql.PostgreSqlCSVWriterConsumer;

import java.io.File;
import java.io.IOException;

public class CSVWriterFactory {

    private CSVWriterFactory(){

    }

    public static CloseableConsumer getCSVWriter(Dialect dialect,File file) throws IOException {
        if (dialect==Dialect.MYSQL){
            return new MySqlCSVWriterConsumer(file);
        }else if (dialect==Dialect.POSTGRESQL){
            return new PostgreSqlCSVWriterConsumer(file);//todo - change me
        }else
            throw new IllegalArgumentException("unknown dialect:"+dialect);
    }

}
