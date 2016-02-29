package com.ccreanga.usecases.export.mysql;

import com.ccreanga.usecases.export.CloseableConsumer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.util.List;


public class MySqlCSVWriterConsumer implements CloseableConsumer<List<Object>> {

    CSVPrinter printer;
    MySqlCSVConvertor mySQLCSVConvertor = new MySqlCSVConvertor();

    public MySqlCSVWriterConsumer(File file) throws IOException {
        printer = new CSVPrinter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"), 1024 * 1024), CSVFormat.MYSQL);
    }

    @Override
    public void accept(List<Object> line) {
        try {
            for (Object next : line) {
                printer.print(mySQLCSVConvertor.toStringConvertor(next));
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
