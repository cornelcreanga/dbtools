package com.ccreanga.usecases.export.postgresql;

import com.ccreanga.usecases.export.CloseableConsumer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.util.List;

public class PostgreSqlCSVWriterConsumer implements CloseableConsumer<List<Object>> {

    CSVPrinter printer;
    PostgreSqlCSVConvertor postgreSqlCSVConvertor = new PostgreSqlCSVConvertor();

    public PostgreSqlCSVWriterConsumer(File file) throws IOException {
        CSVFormat format = CSVFormat.MYSQL.withNullString("\\N").withQuote('"').withQuoteMode(QuoteMode.MINIMAL).withDelimiter(',');
        printer = new CSVPrinter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"), 1024 * 1024), format);
    }

    @Override
    public void accept(List<Object> line) {
        try {
            for (Object next : line) {
                printer.print(postgreSqlCSVConvertor.toStringConvertor(next));
            }
            printer.println();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void close() {
        try {
            printer.close();
        } catch (IOException e) {
            //ignore
        }
    }


}

