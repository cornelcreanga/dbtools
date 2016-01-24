package com.ccreanga.usecases.export;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class DataAnonymizer{

    private boolean anonymize;
    private HashMap tables;

    public DataAnonymizer(String rules) throws FileNotFoundException {
//        URL url = Thread.currentThread().getContextClassLoader().getResource(rules);
//        if (url==null)
//            throw new RuntimeException("can't locate the file "+rules+" in the classpath");
//        InputStream input = new FileInputStream(new File(url.getFile()));
        InputStream input = new FileInputStream(new File(rules));
        Yaml yaml = new Yaml();
        Map<String, Object> data = (Map<String, Object>) yaml.load(input);
        anonymize = (Boolean)data.get("anonymize");
        tables = (HashMap) data.get("data");
    }

    public boolean shouldAnonymize(){
        return anonymize;
    }

    public String processor(String table, String columnName){
        if (!tables.containsKey(table))
            return null;
        List columns = (List) tables.get(table);
        for (Object column : columns) {
            HashMap columnValues = (HashMap) column;
            if (columnValues.get("field").equals(columnName))
                return (String) columnValues.get("type");

        }
        return null;
    }
    public HashMap<String,String> processorSettings(String table, String columnName){
        if (!tables.containsKey(table))
            return null;
        List columns = (List) tables.get(table);
        for (Object column : columns) {
            HashMap columnValues = (HashMap) column;
            if (columnValues.get("field").equals(columnName))
                return (HashMap<String,String>) columnValues.get("values");

        }
        return null;

    }

}
