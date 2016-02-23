package com.ccreanga.usecases.export;

import com.ccreanga.jdbc.BasicModelOperations;
import com.ccreanga.jdbc.DatabaseException;
import com.ccreanga.jdbc.ResultSetOperations;
import com.ccreanga.jdbc.StatementOperations;
import com.ccreanga.jdbc.model.*;
import com.ccreanga.util.FormatUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class MySqlTablesAnonymizer {

    private DataAnonymizer anonymizer;

    public MySqlTablesAnonymizer(DataAnonymizer anonymizer) {
        this.anonymizer = anonymizer;
    }

    public void anonymizeTables(DbConnection readConnection,DbConnection writeConnection, Schema schema) {
        Set<String> tables = anonymizer.getTablesToAnonymize();
        DecimalFormat df = FormatUtil.decimalFormatter();

        BasicModelOperations model = new BasicModelOperations();

        for (String tableName : tables) {
            Optional<Table> optTable = model.getTable(readConnection, schema.getName(), tableName);
            if (!optTable.isPresent()){
                System.out.println("Cannot find the table "+tableName+" in schema "+schema.getName());
                continue;
            }
            Table table = optTable.get();


            Set<String> columns = anonymizer.getTableColumnsToAnonymize(tableName);
            Set<String> tableColumns = model.getColumns(readConnection,schema.getName(),tableName).
                    stream().
                    map(Column::getName).
                    collect(Collectors.toSet());
            boolean skip = false;
            for (String columnToAnonymize : columns) {
                if (!tableColumns.contains(columnToAnonymize)) {
                    System.out.println("table " + tableName + " does not have the column " + columnToAnonymize + "; the anonymizatio on this table was skipped");
                    skip = true;
                    break;
                }
            }
            if (skip)
                continue;

            List<Key> primaryKeys = model.getTablePrimaryKeys(readConnection,schema.getName(),tableName);


            String selectData = "select " +
                    String.join(",", columns.stream().collect(Collectors.toList())) +
                    ","+
                    String.join(",",primaryKeys.stream().map(Key::getColumn).collect(Collectors.toList()))+
                    " from " + table.getName();
            String updateData = "update "+table.getName()+
                    " set "+String.join(",",columns.stream().map(v->v+"=?").collect(Collectors.toList()))+
                    " where "+String.join(" and ",primaryKeys.stream().map(k->k.getColumn()+"=?").collect(Collectors.toList()));
            System.out.println(selectData);
            System.out.println(updateData);

            int columnsNo = columns.size()+primaryKeys.size();

            try (Statement st = readConnection.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                 PreparedStatement ps = writeConnection.getConnection().prepareStatement(updateData)) {
                st.setFetchSize(Integer.MIN_VALUE);//todo - this is just for mysql!
                ResultSet rs = st.executeQuery(selectData);

                int types[] = new int[columnsNo];
                for (int i = 0; i < types.length; i++) {
                    types[i] = rs.getMetaData().getColumnType(i + 1);
                }

                int counter = 1;
                long t1 = System.currentTimeMillis();
                long totalTime = 0, startTime = t1;


                while (rs.next()) {
                    for (int i = 0; i < columnsNo; i++) {
                        try {
                            Object value = ResultSetOperations.readValue(rs, i + 1, types[i]);
                            StatementOperations.setValue(ps,i+1,types[i],value);
                        } catch (Exception e) {
                            throw new DatabaseException(e);
                        }
                    }
                    ps.addBatch();
                    if (counter % 100 == 0)
                        ps.executeBatch();
                    if (counter % 1000 == 0)
                        writeConnection.getConnection().commit();

                    if (counter%10000==0){
                        long time = System.currentTimeMillis() - t1;
                        t1 = System.currentTimeMillis();
                        totalTime = System.currentTimeMillis() - startTime;
                        String message =  "\rProcessing 10000 rows in " + df.format((double)time/1000) + " seconds, total processed lines="+df.format(counter)+", total time="+df.format((double)totalTime/1000);
                        System.out.print(message);
                    }

                    counter++;

                }

                ps.executeBatch();
                writeConnection.getConnection().commit();

            } catch (Exception e) {
                throw new DatabaseException(e);
            }


        }

    }
}
