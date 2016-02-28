package com.ccreanga;

import com.ccreanga.jdbc.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class TestCounter {


    private static class Worker implements Runnable{

        private Connection connection;

        public Worker(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            for(int i=0;i<1;i++) {
                try (Statement st = connection.createStatement()) {
                    st.executeUpdate("update counter set value = value+1");
                    connection.commit();
                } catch (Exception e) {
                    throw new DatabaseException(e);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/test?user=test&password=test");
        connection.setAutoCommit(false);
        Thread[] threads = new Thread[1000];
        for (int i = 0; i < 1000;i++ ) {
            threads[i] = new Thread(new Worker(connection));
        }
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 1000;i++ ) {
            threads[i].start();
        }
        for (Thread t : threads)
            t.join();
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);

    }

}
