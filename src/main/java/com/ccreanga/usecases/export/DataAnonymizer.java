package com.ccreanga.usecases.export;

import com.ccreanga.AnonymizerException;
import com.ccreanga.anonymizer.Anonymizer;
import org.apache.commons.beanutils.BeanUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class DataAnonymizer {

    private boolean anonymize;

    //key = table.column
    private HashMap<String, Anonymizer> anonymizers = new HashMap<>();

    public DataAnonymizer(String rules) {
        InputStream input = null;


        URL url = Thread.currentThread().getContextClassLoader().getResource(rules);
        if (url == null)
            throw new RuntimeException("can't locate the file " + rules + " in the classpath");
        File ruleFile = new File(url.getFile());

        //File ruleFile = new File(rules);
        if (!ruleFile.exists()) {
            throw new RuntimeException("cannot find the file:" + rules);
        }
        try {
            input = new FileInputStream(ruleFile);
        } catch (FileNotFoundException e) {
            System.out.println("cannot found file:" + rules);
        }

        Yaml yaml = new Yaml();
        Map<String, Object> data = null;
        try {
            data = (Map<String, Object>) yaml.load(input);
        } catch (YAMLException e) {
            System.out.println("cannot load file:" + rules + " as a valid yaml file, error is:" + e.getMessage());
            throw new AnonymizerException(e);
        }
        anonymize = (Boolean) data.get("anonymize");
        HashMap<String, List<HashMap>> tables = (HashMap) data.get("data");

        Set<String> tableKeys = tables.keySet();
        for (String tableName : tableKeys) {
            List<HashMap> columns = tables.get(tableName);
            for (HashMap column : columns) {
                String columnName = (String) column.get("field");
                String type = (String) column.get("type");
                HashMap<String, String> values = (HashMap<String, String>) column.get("values");

                Class clazz = null;
                try {
                    clazz = Class.forName(type);
                } catch (ClassNotFoundException e) {
                    System.out.println("cannot found class:" + type);
                    throw new AnonymizerException(e);
                }

                Anonymizer processor = null;
                try {
                    processor = (Anonymizer) clazz.newInstance();
                    if (values != null) {
                        Set<String> keys = values.keySet();
                        for (String next : keys) {
                            BeanUtils.setProperty(processor, next, values.get(next));
                        }
                    }

                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    System.out.println("exception during anonmyzer setup:" + e.getMessage());
                    throw new AnonymizerException(e);
                }

                anonymizers.put(tableName + "." + columnName, processor);
            }
        }
    }

    public boolean shouldAnonymize() {
        return anonymize;
    }

    public Optional<Anonymizer> getAnonymizer(String table, String column) {
        Anonymizer anonymizer = anonymizers.get(table + "." + column);
        if (anonymizer == null)
            return Optional.empty();
        return Optional.of(anonymizer);
    }

    public Set<String> getTablesToAnonymize() {
        Set<String> tables = new HashSet<>();
        Set<String> keys = anonymizers.keySet();
        for (String next : keys) {
            int index = next.indexOf('.');
            tables.add(next.substring(0, index));

        }
        return tables;
    }

    public Set<String> getTableColumnsToAnonymize(String table) {
        Set<String> columns = new LinkedHashSet<>();
        Set<String> keys = anonymizers.keySet();
        for (String next : keys) {
            int index = next.indexOf('.');
            if (table.equals(next.substring(0, index))) {
                columns.add(next.substring(index + 1));
            }
        }
        return columns;
    }


}
