package com.ccreanga;

import com.ccreanga.jdbc.RuntimeSqlException;
import com.ccreanga.random.Language;
import com.ccreanga.random.RandomNameGenerator;
import com.ccreanga.random.RandomNameGeneratorFactory;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class TestHelper {

    private static RandomNameGenerator generator = RandomNameGeneratorFactory.generator(Language.FANTASY);

    public static void dropTables(Connection connection) {
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        URL file = Thread.currentThread().getContextClassLoader().getResource("drop.sql");
        if (file == null)
            throw new RuntimeException("cannot find drop.sql");
        try {
            scriptRunner.runScript(new FileReader(file.getFile()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTables(Connection connection) {
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        URL file = Thread.currentThread().getContextClassLoader().getResource("create.sql");
        if (file == null)
            throw new RuntimeException("cannot find create.sql");
        try {
            scriptRunner.runScript(new FileReader(file.getFile()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void insertTestData(Connection connection, int rows) {

        long counter = 1;
        try(Statement stmt = connection.createStatement()) {

            for (int k = 0; k < rows; k++) {
                String name = generator.compose(2);
                stmt.executeUpdate("INSERT INTO parent(name) VALUES('"+ name + " " + generator.compose(2)+"')",Statement.RETURN_GENERATED_KEYS);
                try(ResultSet rs = stmt.getGeneratedKeys()){
                    if (rs.next()){
                        int id=rs.getInt(1);
                        stmt.executeUpdate("INSERT INTO child(parent_id,name) VALUES("+id+",'"+name + " " + generator.compose(2)+"')",Statement.RETURN_GENERATED_KEYS);
                    }
                }
                if (counter%10000==0)
                    connection.commit();
                counter++;
            }
        } catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }


        counter = 1;
        long t1 = System.currentTimeMillis(), t2;
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO test_types(c_varchar,c_varbinary,c_text,c_blob,c_time,c_timestamp,c_date,c_datetime," +
                        "c_decimal,c_double,c_float,c_bigint,c_int,c_mediumint,c_smallint,c_tinyint)" +
                        " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")
        ) {
            Random random = new Random();

            for (int k = 0; k < rows; k++) {
                if (random.nextInt(100) == 0)
                    ps.setNull(1, Types.VARCHAR);
                else
                    ps.setString(1, generator.compose(2) + " " + generator.compose(2));
                if (random.nextInt(100) == 0)
                    ps.setNull(2, Types.LONGVARBINARY);
                else
                    ps.setBinaryStream(2, new ByteArrayInputStream(generateBytes(300 + random.nextInt(1000))));
                if (random.nextInt(100) == 0)
                    ps.setNull(3, Types.LONGVARCHAR);
                else
                    ps.setCharacterStream(3, new StringReader(generateString(300 + random.nextInt(1000))));
                if (random.nextInt(100) == 0)
                    ps.setNull(4, Types.LONGVARBINARY);
                else
                    ps.setBinaryStream(4, new ByteArrayInputStream(generateBytes(300 + random.nextInt(1000))));
                if (random.nextInt(100) == 0)
                    ps.setNull(5, Types.TIME);
                else
                    ps.setTime(5, new Time(generateDate()));
                if (random.nextInt(100) == 0)
                    ps.setNull(6, Types.TIMESTAMP);
                else
                    ps.setTimestamp(6, new Timestamp(generateDate()));
                if (random.nextInt(100) == 0)
                    ps.setNull(7, Types.DATE);
                else
                    ps.setDate(7, new java.sql.Date(generateDate()));
                if (random.nextInt(100) == 0)
                    ps.setNull(8, Types.TIMESTAMP);
                else
                    ps.setTimestamp(8, new java.sql.Timestamp(generateDate()));
                if (random.nextInt(100) == 0)
                    ps.setNull(9, Types.DECIMAL);
                else
                    ps.setBigDecimal(9, new BigDecimal(10_000_000_000d * random.nextDouble()));
                if (random.nextInt(100) == 0)
                    ps.setNull(10, Types.DOUBLE);
                else
                    ps.setDouble(10, Double.MAX_VALUE * random.nextDouble());
                if (random.nextInt(100) == 0)
                    ps.setNull(11, Types.FLOAT);
                else
                    ps.setFloat(11, Float.MAX_VALUE * random.nextFloat());
                if (random.nextInt(100) == 0)
                    ps.setNull(12, Types.BIGINT);
                else
                    ps.setLong(12, random.nextLong());
                if (random.nextInt(100) == 0)
                    ps.setNull(13, Types.INTEGER);
                else
                    ps.setInt(13, random.nextInt());
                if (random.nextInt(100) == 0)
                    ps.setNull(14, Types.INTEGER);
                else
                    ps.setInt(14, random.nextInt(100_000));
                if (random.nextInt(100) == 0)
                    ps.setNull(15, Types.INTEGER);
                else
                    ps.setInt(15, random.nextInt(10_000));
                if (random.nextInt(100) == 0)
                    ps.setNull(16, Types.INTEGER);
                else
                    ps.setInt(16, random.nextInt(100));

                ps.addBatch();
                if (counter % 100 == 0)
                    ps.executeBatch();
                if (counter % 1000 == 0)
                    connection.commit();
                if (counter % 10000==0) {
                    t2 = System.currentTimeMillis();
                    System.out.println("inserted 10k in:" + (t2 - t1));
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


    public static int randBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    private static byte[] generateBytes(int len) {
        byte[] data = new byte[len];
        new Random().nextBytes(data);
        return data;
    }

    private static String generateString(int len) {
        Random random = new Random();
        char[] chars = "abcdefghijklmnopqrstuvwxyz\n".toCharArray();
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    private static long generateDate() {
        Calendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        int year = randBetween(1975, 2010);
        gc.set(Calendar.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));
        gc.set(Calendar.DAY_OF_YEAR, dayOfYear);
        return gc.getTime().getTime();
    }


}
