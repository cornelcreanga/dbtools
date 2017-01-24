package com.ccreanga.cassandra;

import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.usecases.export.CloseableConsumer;
import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Row;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CassandraWriterConsumer implements CloseableConsumer<List<Object>> {

    CSVPrinter printer;
    CassandraCSVConvertor cassandraCSVConvertor = new CassandraCSVConvertor();
    List<ColumnMetadata> columns;

    public CassandraWriterConsumer(File file, List<ColumnMetadata> columns){
        try {
            printer = new CSVPrinter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"), 1024 * 1024), CSVFormat.MYSQL);
            this.columns = columns;
        } catch (IOException e) {
            throw new IOExceptionRuntime(e);
        }
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void accept(List<Object> values) {
        try {




            printer.println();
        } catch (IOException e) {
            throw new IOExceptionRuntime(e);
        }
    }

}
