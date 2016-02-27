package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Table;
import com.ccreanga.util.FormatUtil;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.ccreanga.util.FormatUtil.readableSize;

public class TableOperations {

    public void processTableRows(DbConnection connection, Table table, List<Column> columns, Consumer<List<Object>> consumer) {

        String selectData = "select " + String.join(",", columns.stream().map(Column::getName).collect(Collectors.toList())) + " from " + table.getName();
        long totalTime = 0, t1 = System.currentTimeMillis(), startTime = t1;
        DecimalFormat df = FormatUtil.decimalFormatter();

        //try to use statistics
        Operations operations = OperationsFactory.createOperations(connection.getDialect());
        long tableSize = operations.getTableSize(connection, table.getSchema(), table.getName());
        long tableRows = operations.getNoOfRows(connection, table.getSchema(), table.getName());

        System.out.println("Table size is " + readableSize(tableSize) + ", estimated number of rows is " + FormatUtil.readableSize(tableRows));

        try (Statement st = connection.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            if (connection.getDialect() == Dialect.MYSQL)
                st.setFetchSize(Integer.MIN_VALUE);//todo - this is just for mysql!
            ResultSet rs = st.executeQuery(selectData);
            int colCount = rs.getMetaData().getColumnCount();
            int types[] = new int[colCount];
            for (int i = 0; i < types.length; i++) {
                types[i] = rs.getMetaData().getColumnType(i + 1);
            }
            int counter = 1;
            while (rs.next()) {
                List<Object> line = new ArrayList<>(colCount);
                for (int i = 0; i < colCount; i++) {
                    try {
                        line.add(ResultSetOperations.readValue(rs, i + 1, types[i]));
                    } catch (Exception e) {
                        throw new DatabaseException(e);
                    }
                }
                consumer.accept(line);
                if (counter % 10000 == 0) {
                    long time = System.currentTimeMillis() - t1;
                    t1 = System.currentTimeMillis();
                    totalTime = System.currentTimeMillis() - startTime;
                    String message = "\rProcessing 10000 rows in " + df.format((double) time / 1000) + " seconds, total processed lines=" + readableSize(counter) + ", total time=" + df.format((double) totalTime / 1000) + " seconds.";
                    System.out.print(message);
                }
                counter++;
            }
            if (counter == 1)
                System.out.print("\rNo rows found");
            else {
                totalTime = System.currentTimeMillis() - startTime;
                System.out.println("\rProcessed " + FormatUtil.readableSize(counter - 1) + " rows in " + df.format((double) totalTime / 1000) + " seconds.");
            }


        } catch (Exception e) {
            throw new DatabaseException(e);
        }

    }

}
