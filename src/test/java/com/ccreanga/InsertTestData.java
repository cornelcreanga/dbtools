package com.ccreanga;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class InsertTestData {

    public static void insertIntoParentChild(Connection c) throws Exception {
        Statement stmt = null;
        long counter = 0;
        long t1 = System.currentTimeMillis(), t2;
        try {
            stmt = c.createStatement();
                    //c.prepareStatement("INSERT INTO parent(name) VALUES(?)");
            for (int k = 0; k < 100; k++) {
                stmt.executeUpdate("INSERT INTO parent(name) VALUES('"+generateString(20)+"')",Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()){
                    int id=rs.getInt(1);
                    stmt.executeUpdate("INSERT INTO child(parent_id,name) VALUES("+id+",'"+generateString(20)+"')",Statement.RETURN_GENERATED_KEYS);
                }
                rs.close();
            }

            c.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getNextException() != null) {
                e.getNextException().printStackTrace();
            }
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

    }

    public static void insertIntoTestTypes(Connection c) throws Exception {
        PreparedStatement ps = null;
        long counter = 0;
        long t1 = System.currentTimeMillis(), t2;
        try {
            ps = c.prepareStatement(
                    "INSERT INTO test_types(c_varchar,c_varbinary,c_text,c_blob,c_time,c_timestamp,c_date,c_datetime," +
                            "c_decimal,c_double,c_float,c_bigint,c_int,c_mediumint,c_smallint,c_tinyint)" +
                    " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            Random random = new Random();
            int x = 1;
            for (int i = 0; i < 1; i++) {
                for (int j = 0; j < 10; j++) {
                    for (int k = 0; k < 100; k++) {
                        if (random.nextInt(100)==0)
                            ps.setNull(1,Types.VARCHAR);
                        else
                            ps.setString(1, generateString(20));
                        if (random.nextInt(100)==0)
                            ps.setNull(2,Types.LONGVARBINARY);
                        else
                            ps.setBinaryStream(2, new ByteArrayInputStream(generateBytes(300+random.nextInt(1000))));
                        if (random.nextInt(100)==0)
                            ps.setNull(3,Types.LONGVARCHAR);
                        else
                            ps.setCharacterStream(3,new StringReader(generateString(300+random.nextInt(1000))));
                        if (random.nextInt(100)==0)
                            ps.setNull(4,Types.LONGVARBINARY);
                        else
                            ps.setBinaryStream(4, new ByteArrayInputStream(generateBytes(300+random.nextInt(1000))));
                        if (random.nextInt(100)==0)
                            ps.setNull(5,Types.TIME);
                        else
                            ps.setTime(5,new Time(generateDate().getTime()));
                        if (random.nextInt(100)==0)
                            ps.setNull(6,Types.TIMESTAMP);
                        else
                            ps.setTimestamp(6,new Timestamp(generateDate().getTime()));
                        if (random.nextInt(100)==0)
                            ps.setNull(7,Types.DATE);
                        else
                            ps.setDate(7,new java.sql.Date(generateDate().getTime()));
                        if (random.nextInt(100)==0)
                            ps.setNull(8,Types.TIMESTAMP);
                        else
                        ps.setTimestamp(8,new java.sql.Timestamp(generateDate().getTime()));
                        if (random.nextInt(100)==0)
                            ps.setNull(9,Types.DECIMAL);
                        else
                            ps.setBigDecimal(9,new BigDecimal(10_000_000_000d*random.nextDouble()));
                        if (random.nextInt(100)==0)
                            ps.setNull(10,Types.DOUBLE);
                        else
                            ps.setDouble(10, Double.MAX_VALUE*random.nextDouble());
                        if (random.nextInt(100)==0)
                            ps.setNull(11,Types.FLOAT);
                        else
                            ps.setFloat(11, Float.MAX_VALUE*random.nextFloat());
                        if (random.nextInt(100)==0)
                            ps.setNull(12,Types.BIGINT);
                        else
                            ps.setLong(12,random.nextLong());
                        if (random.nextInt(100)==0)
                            ps.setNull(13,Types.INTEGER);
                        else
                            ps.setInt(13,random.nextInt());
                        if (random.nextInt(100)==0)
                            ps.setNull(14,Types.INTEGER);
                        else
                            ps.setInt(14,random.nextInt(100_000));
                        if (random.nextInt(100)==0)
                            ps.setNull(15,Types.INTEGER);
                        else
                            ps.setInt(15,random.nextInt(10_000));
                        if (random.nextInt(100)==0)
                            ps.setNull(16,Types.INTEGER);
                        else
                            ps.setInt(16,random.nextInt(100));

                        ps.addBatch();
                        counter++;
                    }
                    ps.executeBatch();
                }
                c.commit();
                if (counter == 10000) {
                    t2 = System.currentTimeMillis();
                    System.out.println("inserted:" + counter + " in:" + (t2 - t1));
                    t1 = t2;
                    counter = 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getNextException() != null) {
                e.getNextException().printStackTrace();
            }
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }

    }


    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }
    private static byte[] generateBytes(int len){
        byte[] data = new byte[len];
        new Random().nextBytes(data);
        return data;
    }

    private static String generateString(int len){
        Random random = new Random();
        char[] chars = "abcdefghijklmnopqrstuvwxyz\n".toCharArray();
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    private static Date generateDate(){
        Calendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        int year = randBetween(1975, 2010);
        gc.set(Calendar.YEAR, year);
        int dayOfYear = randBetween(1, gc.getActualMaximum(Calendar.DAY_OF_YEAR));
        gc.set(Calendar.DAY_OF_YEAR, dayOfYear);
        return gc.getTime();
    }

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test?user=root&password=root&zeroDateTimeBehavior=convertToNull");
        connection.setAutoCommit(false);

        insertIntoTestTypes(connection);
        insertIntoParentChild(connection);

    }

}
