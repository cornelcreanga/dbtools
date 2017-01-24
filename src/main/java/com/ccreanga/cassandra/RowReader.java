package com.ccreanga.cassandra;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.Row;

import java.util.ArrayList;
import java.util.List;

public class RowReader {

    public List<Object> readRow(Row row,List<ColumnMetadata> columns){
        List<Object> results = new ArrayList<>(columns.size());
        for (int i = 0; i < columns.size(); i++) {
            DataType type = columns.get(i).getType();
            if (type.equals(DataType.ascii()))
                results.add(row.getString(i));
            else if (type.equals(DataType.bigint()))
                results.add(row.getString(i));//todo
        }
        return null;
    }

}
