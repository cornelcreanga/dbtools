package com.ccreanga.cassandra;

import com.ccreanga.GenericConfig;
import com.datastax.driver.core.*;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.ccreanga.util.FormatUtil.formatMillis;
import static com.ccreanga.util.FormatUtil.readableSize;

public class CassandraOperations {

    public void processTableRows(Session session, TableMetadata table, List<ColumnMetadata> columns, Consumer<List<Object>> consumer) {
        String selectData = String.format("select %s from %s",
                String.join(",", columns.stream().map(ColumnMetadata::getName).collect(Collectors.toList())),
                table.getName());
        SimpleStatement statement = new SimpleStatement(selectData);
        statement.setFetchSize(100);
        long totalTime = 0, t1 = System.currentTimeMillis(), startTime = t1;
        ResultSet rs = session.execute(statement);
        int counter = 1;
        while (!rs.isExhausted()) {
            Row row = rs.one();
            consumer.accept(new RowReader().readRow(row, columns));
            if (counter % GenericConfig.progress == 0) {
                long time = System.currentTimeMillis() - t1;
                t1 = System.currentTimeMillis();
                totalTime = System.currentTimeMillis() - startTime;
                System.out.printf("\rProcessing %s rows in %s seconds, total processed lines=%s, total time=%s seconds.",
                        GenericConfig.progress,
                        formatMillis(time),
                        readableSize(counter),
                        formatMillis(totalTime)
                );
            }
            counter++;

        }
        if (counter == 1)
            System.out.print("\rNo rows found");
        else {
            totalTime = System.currentTimeMillis() - startTime;
            System.out.printf("\rProcessed %s rows in %s seconds.\n",
                    readableSize(counter - 1),
                    formatMillis(totalTime));

        }


    }
}