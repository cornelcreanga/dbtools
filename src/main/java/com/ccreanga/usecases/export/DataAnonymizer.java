package com.ccreanga.usecases.export;

import com.ccreanga.AnonymizerException;
import com.ccreanga.anonymizer.Anonymizer;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("unchecked")
public class DataAnonymizer {

    private boolean anonymize;

    private HashMap<String, Anonymizer> anonymizers = new HashMap<>();

    public DataAnonymizer(String fileRules) {
        try (InputStream input = new FileInputStream(fileRules)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = null;
            try {
                data = (Map<String, Object>) yaml.load(input);
            } catch (YAMLException e) {
                System.out.println("cannot load file:" + fileRules + " as a valid yaml file, error is:" + e.getMessage());
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
                                setProperty(processor, next, values.get(next));
                            }
                        }

                    } catch (InstantiationException | IllegalAccessException e) {
                        System.out.println("exception during anonmyzer setup:" + e.getMessage());
                        throw new AnonymizerException(e);
                    }

                    anonymizers.put(tableName + "." + columnName, processor);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("cannot read from file:" + fileRules);
        }

    }

    private void setProperty(Object object, String name, Object value) {
        Class<?> clazz = object.getClass();
        String methodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
        try {
            Method method = getMethod(clazz, methodName);
            if (method == null)
                throw new AnonymizerException("cannot find the method " + methodName + "(parameter) in class" + clazz.getName());
            method.invoke(object, value);
        } catch (InvocationTargetException e) {
            throw new AnonymizerException("methodName:" + methodName + " throwed an exception", e);
        } catch (IllegalAccessException e) {
            throw new AnonymizerException("methodName:" + methodName + " cannot be invoked", e);
        }
    }

    private static Method getMethod(Class<?> clazz, String methodName) {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if ((method.getName().equals(methodName)) && (method.getParameterCount() == 1)) {
                return method;
            }
        }
        return null;

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
