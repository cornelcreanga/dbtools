package com.ccreanga;

import com.ccreanga.jdbc.Dialect;
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
import java.util.Random;

import static com.ccreanga.RandomUtil.*;

public class PostgreSqlHelper {

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

        try (PreparedStatement psParent = connection.prepareStatement("INSERT INTO parent(name) VALUES(?)", PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement psChild = connection.prepareStatement("INSERT INTO child(parent_id,name) VALUES(?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int k = 1; k <= rows; k++) {
                String name = generator.compose(2);
                psParent.setString(1, name);
                psParent.addBatch();

                if (k % 10000 == 0) {
                    executeBatch(psParent, psChild);
                    connection.commit();
                }
            }

            executeBatch(psParent, psChild);

            connection.commit();

            t2 = System.currentTimeMillis();
            System.out.println("inserted " + rows * 2 + " in:" + (t2 - t1) + " ms");

        } catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }


        counter = 1;
        t1 = System.currentTimeMillis();
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO test_types(c_varchar,c_text,c_blob,c_time,c_timestamp,c_date," +
                        "c_decimal,c_double,c_float,c_bigint,c_int,c_smallint)" +
                        " VALUES(?,?,?,?,?,?,?,?,?,?,?,?)")
        ) {
            Random random = new Random();

            for (int k = 0; k < rows; k++) {
                if (random.nextInt(100) == 0)
                    ps.setNull(1, Types.VARCHAR);
                else
                    ps.setString(1, generator.compose(2) + " " + generator.compose(2));

                if (random.nextInt(100) == 0)
                    ps.setNull(2, Types.LONGVARCHAR);
                else
                    ps.setString(2, generateString(300 + random.nextInt(1000)));
                if (random.nextInt(100) == 0)
                    ps.setNull(3, Types.LONGVARBINARY);
                else {
                    int len = 300 + random.nextInt(1000);
                    ps.setBytes(3, generateBytes(len));
                }

                if (random.nextInt(100) == 0)
                    ps.setNull(4, Types.TIME);
                else
                    ps.setTime(4, new Time(generateDate()));
                if (random.nextInt(100) == 0)
                    ps.setNull(5, Types.TIMESTAMP);
                else
                    ps.setTimestamp(5, new Timestamp(generateDate()));
                if (random.nextInt(100) == 0)
                    ps.setNull(6, Types.DATE);
                else
                    ps.setDate(6, new java.sql.Date(generateDate()));
                if (random.nextInt(100) == 0)
                    ps.setNull(7, Types.DECIMAL);
                else
                    ps.setBigDecimal(7, new BigDecimal(10_000_000_000d * random.nextDouble()));
                if (random.nextInt(100) == 0)
                    ps.setNull(8, Types.DOUBLE);
                else
                    ps.setDouble(8, Double.MAX_VALUE * random.nextDouble());
                if (random.nextInt(100) == 0)
                    ps.setNull(9, Types.FLOAT);
                else
                    ps.setFloat(9, Float.MAX_VALUE * random.nextFloat());
                if (random.nextInt(100) == 0)
                    ps.setNull(10, Types.BIGINT);
                else
                    ps.setLong(10, random.nextLong());
                if (random.nextInt(100) == 0)
                    ps.setNull(11, Types.INTEGER);
                else
                    ps.setInt(11, random.nextInt());
                if (random.nextInt(100) == 0)
                    ps.setNull(12, Types.INTEGER);
                else
                    ps.setInt(12, random.nextInt(100));

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

    private static void executeBatch(PreparedStatement psParent, PreparedStatement psChild) throws SQLException {
        psParent.executeBatch();
        ResultSet rs = psParent.getGeneratedKeys();
        while (rs.next()) {
            int id= rs.getInt(1);
            psChild.setInt(1, id);
            psChild.setString(2, generator.compose(2));
            psChild.addBatch();

        }
        psChild.executeBatch();
    }


    public static void handleSqlException(Exception e) {
        SQLException exception = (SQLException) e.getCause();
        while (exception != null) {
            exception.printStackTrace();
            exception = exception.getNextException();
        }

    }


}
