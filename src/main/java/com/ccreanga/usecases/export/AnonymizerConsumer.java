package com.ccreanga.usecases.export;

import com.ccreanga.anonymizer.Anonymizer;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.Table;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class AnonymizerConsumer implements Consumer<List<Object>> {

    private DataAnonymizer dataAnonymizer;
    private String tableName;
    private List<String> columns;

    public AnonymizerConsumer(DataAnonymizer dataAnonymizer, String tableName, List<String> columns) {
        this.dataAnonymizer = dataAnonymizer;
        this.tableName = tableName;
        this.columns = columns;
    }

    @Override
    public void accept(List<Object> objects) {
        if (dataAnonymizer != null && dataAnonymizer.shouldAnonymize()) {
            int index = 0;
            for (String column : columns) {
                Optional<Anonymizer> optional = dataAnonymizer.getAnonymizer(tableName, column);
                if (optional.isPresent()) {
                    Anonymizer anonymizer = optional.get();
                    Object original = objects.get(index);
                    if (original != null)
                        objects.set(index, anonymizer.anonymize(original,objects));
                }
                index++;
            }

        }
    }
}
