package com.ccreanga.usecases.export;

import com.ccreanga.anonymizer.Anonymizer;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.Table;
import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class AnonymizerConsumer implements Consumer<List<Object>> {

    private DataAnonymizer anonymizer;
    private Table table;

    public AnonymizerConsumer(DataAnonymizer anonymizer, Table table) {
        this.anonymizer = anonymizer;
        this.table = table;
    }

    @Override
    public void accept(List<Object> objects) {
        if (anonymizer.shouldAnonymize()){
            List<Column> columns = table.getColumns();
            int index = 0;
            for (Column column : columns) {
                String processorName = anonymizer.processor(table.getName(),column.getName());
                if (processorName!=null){
                    try {
                        Class clazz = Class.forName(processorName);
                        Anonymizer processor = (Anonymizer) clazz.newInstance();
                        HashMap<String,String> processorSettings = anonymizer.processorSettings(table.getName(), column.getName());
                        Set<String> keys = processorSettings.keySet();
                        for (String next : keys) {
                            BeanUtils.setProperty(processor,next,processorSettings.get(next));
                        }
                        Object original = objects.get(index);
                        objects.set(index,processor.anonymize(original));
                    }catch (Exception e){
                        throw new RuntimeException(e);
                    }

                }
                index++;
            }

        }
    }
}
