package com.ccreanga.usecases.export.jdbc.oracle;

import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.jdbc.oracle.LobFiles;
import com.ccreanga.jdbc.oracle.OracleScriptGenerator;
import com.ccreanga.usecases.export.jdbc.CloseableConsumer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.util.List;

//http://docs.oracle.com/cd/B19306_01/server.102/b14215/ldr_loading.htm#sthref1368 todo
public class OracleCSVWriterConsumer implements CloseableConsumer<List<Object>> {

    CSVPrinter printer;
    OracleCSVConvertor oracleCSVConvertor;
    String table;
    List<String> columns;


    public OracleCSVWriterConsumer(File file,String table,List<String> columns) {
        try {
            printer = new CSVPrinter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"), 1024 * 1024), CSVFormat.MYSQL);
            oracleCSVConvertor = new OracleCSVConvertor();
            this.table = table;
            this.columns = columns;
        } catch (IOException e) {
            throw new IOExceptionRuntime(e);
        }
    }

    @Override
    public void accept(List<Object> line) {
        try {
            for (int i = 0; i < line.size(); i++) {
                Object next =  line.get(i);
                printer.print(oracleCSVConvertor.toStringConvertor(
                        next,
                        LobFiles.getOutputStream(table,columns.get(i)),
                        OracleUtil.generateLobBoundary(table+"###"+columns.get(i))
                ));
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
