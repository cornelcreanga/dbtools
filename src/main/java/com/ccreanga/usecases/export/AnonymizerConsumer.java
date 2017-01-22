package com.ccreanga.usecases.export;

import com.ccreanga.anonymizer.Anonymizer;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.Table;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class AnonymizerConsumer implements Consumer<List<Object>> {

    private DataAnonymizer dataAnonymizer;
    private Table table;
    private List<Column> tableColumns;

    public AnonymizerConsumer(DataAnonymizer dataAnonymizer, Table table, List<Column> tableColumns) {
        this.dataAnonymizer = dataAnonymizer;
        this.table = table;
        this.tableColumns = tableColumns;
    }

    @Override
    public void accept(List<Object> objects) {
        if (dataAnonymizer != null && dataAnonymizer.shouldAnonymize()) {
            int index = 0;
            for (Column column : tableColumns) {
                Optional<Anonymizer> optional = dataAnonymizer.getAnonymizer(table.getName(), column.getName());
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
