package com.ccreanga.jdbc;

import com.ccreanga.GenericConfig;
import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Table;
import com.ccreanga.util.FormatUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.ccreanga.util.FormatUtil.formatMillis;
import static com.ccreanga.util.FormatUtil.readableSize;

public class TableOperations {

    public void processTableRows(DbConnection connection, Table table, List<Column> columns, Consumer<List<Object>> consumer) {
        String selectData = String.format("select %s from %s",
                String.join(",", columns.stream().map(Column::getName).collect(Collectors.toList())),
                table.getName());
        long totalTime = 0, t1 = System.currentTimeMillis(), startTime = t1;

        //try to use statistics
        Operations operations = OperationsFactory.createOperations(connection.getDialect());
        long tableSize = operations.getTableSize(connection, table.getSchema(), table.getName());
        long tableRows = operations.getNoOfRows(connection, table.getSchema(), table.getName());

        System.out.println("Table size is " + readableSize(tableSize) + ", estimated number of rows is " + FormatUtil.readableSize(tableRows));

        boolean close = true;
        ResultSet rs = null;
        Statement st = null;
        try {
            st = connection.getConnection().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (connection.getDialect() == Dialect.MYSQL)
                st.setFetchSize(Integer.MIN_VALUE);//enable mysql streaming
            else
                st.setFetchSize(100);
            rs = st.executeQuery(selectData);
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

        } catch (IOExceptionRuntime e) {
            operations.forceDiscardResultSetAndCloseConnection(connection, rs);
            close = false;
            throw e;
            //force discard rs, close connection
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            try {
                if ((st != null) && close)
                    st.close();
            } catch (SQLException e) {
            }
        }

    }

}
