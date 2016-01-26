package com.ccreanga.usecases.export;

import com.ccreanga.anonymizer.Anonymizer;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.Table;
import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class AnonymizerConsumer implements Consumer<List<Object>> {

    private DataAnonymizer dataAnonymizer;
    private Table table;

    public AnonymizerConsumer(DataAnonymizer dataAnonymizer, Table table) {
        this.dataAnonymizer = dataAnonymizer;
        this.table = table;
    }

    @Override
    public void accept(List<Object> objects) {
        if (dataAnonymizer!=null && dataAnonymizer.shouldAnonymize()){
            List<Column> columns = table.getColumns();

            int index = 0;
            for (Column column : columns) {
                Optional<Anonymizer> optional = dataAnonymizer.getAnonymizer(table.getName(),column.getName());
                if (optional.isPresent()){
                    Anonymizer anonymizer = optional.get();
                    Object original = objects.get(index);
                    if (original!=null)
                        objects.set(index,anonymizer.anonymize(original));
                }
            }

        }
    }
}
