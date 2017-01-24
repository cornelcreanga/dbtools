package com.ccreanga.cassandra;

import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.usecases.export.CloseableConsumer;
import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Row;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.util.List;

public class CassandraWriterConsumer implements CloseableConsumer<List<Object>> {

    CSVPrinter printer;
    CassandraCSVConvertor cassandraCSVConvertor = new CassandraCSVConvertor();
    List<ColumnMetadata> columns;

    public CassandraWriterConsumer(File file, List<ColumnMetadata> columns){
        CSVFormat format = CSVFormat.MYSQL.withNullString("").withEscape('\\').withQuote('"').withQuoteMode(QuoteMode.MINIMAL).withDelimiter(',');

        try {
            printer = new CSVPrinter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream(file), "UTF-8"), 1024 * 1024), format);
            this.columns = columns;
        } catch (IOException e) {
            throw new IOExceptionRuntime(e);
        }
    }

    @Override
    public void close() throws IOException {
        try {
            printer.close();
        } catch (IOException e) {
            //ignore
        }
    }

    @Override
    public void accept(List<Object> line) {
        try {
            for (Object next : line) {
                printer.print(cassandraCSVConvertor.toStringConvertor(next));
            }
            printer.println();
        } catch (IOException e) {
            throw new IOExceptionRuntime(e);
        }
    }

}
