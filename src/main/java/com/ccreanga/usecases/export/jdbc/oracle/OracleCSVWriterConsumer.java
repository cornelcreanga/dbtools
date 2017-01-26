package com.ccreanga.usecases.export.jdbc.oracle;

import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.usecases.export.jdbc.CloseableConsumer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.util.List;


public class OracleCSVWriterConsumer implements CloseableConsumer<List<Object>> {

    CSVPrinter printer;
    OracleCSVConvertor oracleCSVConvertor = new OracleCSVConvertor();

    public OracleCSVWriterConsumer(File file) {
        try {
            printer = new CSVPrinter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"), 1024 * 1024), CSVFormat.MYSQL);
        } catch (IOException e) {
            throw new IOExceptionRuntime(e);
        }
    }

    @Override
    public void accept(List<Object> line) {
        try {
            for (Object next : line) {
                printer.print(oracleCSVConvertor.toStringConvertor(next));
            }
            printer.println();
        } catch (IOException e) {
            throw new IOExceptionRuntime(e);
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
