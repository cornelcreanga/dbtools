package com.ccreanga;

import com.ccreanga.jdbc.RuntimeSqlException;
import com.ccreanga.random.Language;
import com.ccreanga.random.RandomNameGenerator;
import com.ccreanga.random.RandomNameGeneratorFactory;
import com.ccreanga.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.ccreanga.RandomUtil.*;

public class OracleHelper {

    private static RandomNameGenerator generator = RandomNameGeneratorFactory.generator(Language.FANTASY);

    public static void runSqlFile(Connection connection, String fileName) {
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        InputStream in = FileUtil.classPathResource(fileName);
        try {
            scriptRunner.runScript(new InputStreamReader(in, "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertTestData(Connection connection, int rows) throws SQLException {

        if (rows > 100_000)
            throw new IllegalArgumentException("rows should be lower than 100000");

        long counter;
        long t1 = System.currentTimeMillis(), t2;

        try (PreparedStatement psParent = connection.prepareStatement("INSERT INTO parent(name) VALUES(?)", new String[]{"id"});
             PreparedStatement psChild = connection.prepareStatement("INSERT INTO child(parent_id,name) VALUES(?,?)")) {

            int returnedId = 0;
            for (int k = 1; k <= rows; k++) {

                String name = generator.compose(2);
                psParent.setString(1, name);
                psParent.executeUpdate();

                ResultSet idRs = psParent.getGeneratedKeys();
                if (idRs.next()){
                    returnedId = idRs.getInt(1);
                }else
                    throw new RuntimeException("can't obtain the generated keys");

                psChild.setInt(1, returnedId);
                psChild.setString(2, generator.compose(2));
                psChild.addBatch();

                if (k % 10000 == 0) {
                    psChild.executeBatch();
                    connection.commit();
                }
            }

            psChild.executeBatch();

            connection.commit();

            t2 = System.currentTimeMillis();
            System.out.println("inserted " + rows * 2 + " in:" + (t2 - t1) + " ms");

        } catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }


        counter = 1;
        t1 = System.currentTimeMillis();
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO test_types(c_varchar2,c_nvarchar2,c_clob,c_nclob,c_number,c_double,c_float,c_date," +
                        "c_timestamp,c_timestamptz,c_blob)" +
                        " VALUES(?,?,?,?,?,?,?,?,?,?,?)")

        ) {
            Random random = new Random();

            for (int k = 0; k < rows; k++) {
                if (random.nextInt(100) == 0)
                    ps.setNull(1, Types.VARCHAR);
                else
                    ps.setString(1, generator.compose(2) + " " + generator.compose(2));
                if (random.nextInt(100) == 0)
                    ps.setNull(2, Types.NVARCHAR);
                else
                    ps.setString(2, generator.compose(2) + " " + generator.compose(2));

                if (random.nextInt(100) == 0)
                    ps.setNull(3, Types.LONGVARCHAR);
                else
                    ps.setString(3, generateString(300 + random.nextInt(1000)));

                if (random.nextInt(100) == 0)
                    ps.setNull(4, Types.LONGNVARCHAR);
                else
                    ps.setString(4, generateString(300 + random.nextInt(1000)));

                if (random.nextInt(100) == 0)
                    ps.setNull(5, Types.DOUBLE);
                else
                    ps.setBigDecimal(5, new BigDecimal(10_000_000_000d * random.nextDouble()));

                if (random.nextInt(100) == 0)
                    ps.setNull(6, Types.DOUBLE);
                else
                    ps.setDouble(6, Float.MAX_VALUE * random.nextDouble());

                if (random.nextInt(100) == 0)
                    ps.setNull(7, Types.FLOAT);
                else
                    ps.setFloat(7, Float.MAX_VALUE * random.nextFloat());


                if (random.nextInt(100) == 0)
                    ps.setNull(8, Types.TIMESTAMP);
                else
                    ps.setTimestamp(8, new Timestamp(generateDate()));


                if (random.nextInt(100) == 0)
                    ps.setNull(9, Types.TIMESTAMP);
                else
                    ps.setTimestamp(9, new Timestamp(generateDate()));

                if (random.nextInt(100) == 0)
                    ps.setNull(10, Types.TIMESTAMP);
                else
                    ps.setTimestamp(10, new Timestamp(generateDate()));

                if (random.nextInt(100) == 0)
                    ps.setNull(11, Types.LONGVARBINARY);
                else {
                    int len = 300 + random.nextInt(1000);
                    ps.setBytes(11, generateBytes(len));
                }

//                SQLXML xml = connection.createSQLXML();
//                xml.setString("<?xml version=\"1.0\" encoding=\"UTF-8\"?><configuration>test</configuration>");
//                ps.setSQLXML(12,xml);

                ps.addBatch();
                if (counter % 500 == 0)
                    ps.executeBatch();
                if (counter % 1000 == 0)
                    connection.commit();
                if (counter % 1000 == 0) {
                    t2 = System.currentTimeMillis();
                    System.out.println("inserted 1k in:" + (t2 - t1) + " ms");
                    t1 = t2;
                }
                counter++;
            }
            ps.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }


    public static void handleSqlException(Exception e) {
        SQLException exception = (SQLException) e.getCause();
        while (exception != null) {
            exception.printStackTrace();
            exception = exception.getNextException();
        }

    }


}
