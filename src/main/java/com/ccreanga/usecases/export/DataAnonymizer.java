package com.ccreanga.usecases.export;

import com.ccreanga.random.RandomNameGenerator;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class DataAnonymizer implements Consumer<List<Object>> {


    private class TablesOption{
        HashMap tables;
        public TablesOption(HashMap tables) {
            this.tables=tables;
        }
        public String anonymizer(String table, String columnName){
            if (!tables.containsKey(table))
                return null;
            List columns = (List) tables.get("table");
            for (Object column : columns) {
                HashMap columnValues = (HashMap) column;
                if (columnValues.get("field").equals(columnName))
                    return (String) columnValues.get("type");

            }
            return null;
        }
        public HashMap anonymizerSetting(String table, String columnName){
            if (!tables.containsKey(table))
                return null;
            List columns = (List) tables.get("table");
            for (Object column : columns) {
                HashMap columnValues = (HashMap) column;
                if (columnValues.get("field").equals(columnName))
                    return (HashMap) columnValues.get("values");

            }
            return null;

        }

    }



    public DataAnonymizer(String rules) throws FileNotFoundException {

        URL url = Thread.currentThread().getContextClassLoader().getResource(rules);
        if (url==null)
            throw new RuntimeException("can't locate the file "+rules+" in the classpath");
        InputStream input = new FileInputStream(new File(url.getFile()));
        Yaml yaml = new Yaml();
        Map<String, Object> data = (Map<String, Object>) yaml.load(input);
        HashMap tables = (HashMap) data.get("data");
        Set keys = tables.keySet();
        for (Object next : keys) {
            System.out.println("table:"+next);
            List columns = (List) tables.get(next);
            System.out.println(columns);
        }
    }

    @Override
    public void accept(List<Object> objects) {

    }
}
