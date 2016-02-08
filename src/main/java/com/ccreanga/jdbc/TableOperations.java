package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Table;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TableOperations {

    public void processTableRows(DbConnection connection, Table table, Consumer<List<Object>> consumer) {

        String selectData = "select " + String.join(",", table.getColumns().stream().map(Column::getName).collect(Collectors.toList())) + " from " + table.getName();
        //selectData +=" where id=12";
        long t1 = System.currentTimeMillis();
        long totalTime = 0, startTime = t1;

        DecimalFormat df = new DecimalFormat("###.###");
        df.setGroupingUsed(true);
        df.setGroupingSize(3);


        try (Statement st = connection.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            st.setFetchSize(Integer.MIN_VALUE);//todo - this is just for mysql!
            ResultSet rs = st.executeQuery(selectData);
            int colCount = rs.getMetaData().getColumnCount();
            int types[] = new int[colCount];
            for (int i = 0; i < types.length; i++) {
                types[i] = rs.getMetaData().getColumnType(i + 1);
            }
            int counter=1;
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
                if (counter%10000==0){
                    long time = System.currentTimeMillis() - t1;
                    t1 = System.currentTimeMillis();
                    totalTime = System.currentTimeMillis() - startTime;
                    String message =  "\rProcessing 10000 rows in " + df.format((double)time/1000) + " seconds, total processed lines="+df.format(counter)+", total time="+df.format((double)totalTime/1000);
                    System.out.print(message);
                }
                counter++;
            }
            if (counter==1)
                System.out.print("\rno rows found");


        } catch (Exception e) {
            throw new DatabaseException(e);
        }

    }

}
