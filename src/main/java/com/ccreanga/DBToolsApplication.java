package com.ccreanga;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.ccreanga.jdbc.DatabaseException;
import com.ccreanga.jdbc.Dialect;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.usecases.export.DataAnonymizer;
import com.ccreanga.usecases.export.cassandra.CassandraExport;
import com.ccreanga.usecases.export.jdbc.SqlTablesExport;
import com.ccreanga.usecases.info.SqlTablesInfo;
import com.ccreanga.usecases.process.SqlTablesAnonymizer;
import com.ccreanga.util.ConsoleUtil;
import com.ccreanga.util.FormatUtil;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBToolsApplication {


    private static final String HOST = "host";
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String SCHEMA = "schema";
    private static final String EXPORT = "export";
    private static final String AN = "an";
    private static final String DIALECT = "dialect";

    /**
     * -an /home/cornel/projects/dbtools/src/main/resources/an.yml -host localhost -password root -schema test -user root -export * /tmp y
     * -an /home/cornel/projects/dbtools/src/main/resources/an.yml -dialect POSTGRESQL -host localhost -password test -schema test -user test -export * /tmp y
     *
     */
    public static void main(String[] args) {

        Parameters p = new Parameters();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(p)
                .programName("dbtools")
                .acceptUnknownOptions(true)
                .build();
        try {
            jCommander.parse(args);
            if (p.help){
                jCommander.usage();
                return;
            }
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        String host=p.getHost(), schema=p.getSchema(), user=p.getUser(),export=p.getExport(),anonymize=p.getAnonymize(),info=p.getInfo();
        Dialect dialect = Dialect.MYSQL;

        if (p.getDialect()!=null) {
            try {
                dialect = Dialect.valueOf(p.getDialect());
            } catch (IllegalArgumentException e) {
                exit("Don't understand dialect " + p.getDialect(),jCommander);
            }
        } else {
            System.out.println("No dialect specified - using by default " + dialect);
        }



            char[] password;
            if (p.getPassword()==null) {
                password = ConsoleUtil.readPassword("[%s]", "Password:");
            } else {
                password = p.getPassword().toCharArray();
            }


        //anonymize database
        if (anonymize!=null && export==null) {
            try (
                    DbConnection readConnection = connection(dialect, host, schema, user, password, true);
                    DbConnection writeConnection = connection(dialect, host, schema, user, password, false);
            ) {
                anonymizeDatabase(readConnection, writeConnection, schema, new DataAnonymizer(anonymize));
            } catch (Exception e) {
                System.out.println("An exception occured during the anonymization process, the full stracktrace is");
                e.printStackTrace();//todo
            }
            return;
        }

        //generate export files and optionally anonymize them
        if (export!=null) {
            DataAnonymizer dataAnonymizer = null;
            if (anonymize!=null) {
                dataAnonymizer = new DataAnonymizer(anonymize);
            }
            String[] exportParams = p.getExport().split(" ");

            if (dialect.isSQL()) {
                try (DbConnection connection = connection(dialect, host, schema, user, password, true)) {
                    exportSQLDatabase(connection, schema, exportParams[0], exportParams[1], booleanParam(exportParams[2]), dataAnonymizer);
                } catch (DatabaseException | IOExceptionRuntime e) {
                    System.out.println("Finished execution due to unexpected error.");
                }
            } else if (dialect==Dialect.CASSANDRA) {
                System.out.println("cassandra support is not yet properly implemented");
                System.exit(1);
                try (Session session = session(host, schema, user, password)) {
                    exportCassandra(session, schema, exportParams[0], exportParams[1], booleanParam(exportParams[2]), dataAnonymizer);
                }
            }
        }

        if (info!=null){
            if (dialect.isSQL()){
                try (DbConnection connection = connection(dialect, host, schema, user, password, true)) {
                    infoSQLDatabase(connection, schema, info);
                } catch (DatabaseException | IOExceptionRuntime e) {
                    System.out.println("Finished execution due to unexpected error.");
                }

            }

        }

    }

    private static boolean booleanParam(String s) {
        return s.equalsIgnoreCase("y") || s.equalsIgnoreCase("yes");
    }

    private static void anonymizeDatabase(DbConnection readConnection, DbConnection writeConnection, String schema, DataAnonymizer dataAnonymizer) {
        SqlTablesAnonymizer sqlTablesAnonymizer = new SqlTablesAnonymizer(dataAnonymizer);

        long t1 = System.currentTimeMillis();
        sqlTablesAnonymizer.anonymizeTables(readConnection, writeConnection, new Schema(schema));
        long t2 = System.currentTimeMillis();
        System.out.println("Anonymization finished in " + FormatUtil.formatMillis(t2 - t1) + " seconds.");

    }

    private static void exportSQLDatabase(DbConnection connection, String schema, String pattern, String folder, boolean overwrite, DataAnonymizer dataAnonymizer) {
        SqlTablesExport sqlTablesExport = dataAnonymizer == null ?
                new SqlTablesExport() :
                new SqlTablesExport(dataAnonymizer);

        long t1 = System.currentTimeMillis();
        sqlTablesExport.exportTables(connection, new Schema(schema), pattern, folder, overwrite);
        long t2 = System.currentTimeMillis();
        System.out.println("Export finished in " + FormatUtil.formatMillis(t2 - t1) + " seconds.");
    }

    private static void infoSQLDatabase(DbConnection connection, String schema, String pattern) {
        SqlTablesInfo info = new SqlTablesInfo();
        long t1 = System.currentTimeMillis();
        info.info(connection, new Schema(schema), pattern);
        long t2 = System.currentTimeMillis();
        System.out.println("Export finished in " + FormatUtil.formatMillis(t2 - t1) + " seconds.");

    }

    private static void exportCassandra(Session session, String keyspace, String pattern, String folder, boolean overwrite, DataAnonymizer dataAnonymizer) {
        CassandraExport cassandraExport = dataAnonymizer == null ?
                new CassandraExport() :
                new CassandraExport(dataAnonymizer);

        long t1 = System.currentTimeMillis();
        cassandraExport.exportTables(session, keyspace, pattern, folder, overwrite);
        long t2 = System.currentTimeMillis();
        System.out.println("Export finished in " + FormatUtil.formatMillis(t2 - t1) + " seconds.");
    }


    private static Session session(String contactPoint, String keyspace, String user, char[] password) {

        Cluster.Builder builder = Cluster.builder()
                .addContactPoint(contactPoint);//todo- with port
        if (user != null)
            builder = builder.withCredentials(user, new String(password));

        return builder.build().connect(keyspace);

    }

    private static DbConnection connection(Dialect dialect, String host, String schema, String user, char[] password, boolean readOnly) {
        try {
            Connection connection;
            if (dialect == Dialect.MYSQL) {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(
                        String.format("jdbc:mysql://%s/%s?user=%s&password=%s&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=%s",
                                host, schema, user, new String(password), MySqlConfig.rewriteBatchedStatements));
            } else if (dialect == Dialect.POSTGRESQL) {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(
                        String.format("jdbc:postgresql://%s/%s?user=%s&password=%s", host, schema, user, new String(password)));
            } else if (dialect == Dialect.SQL_SERVER) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                String connectionString =
                        "jdbc:sqlserver://your_servername.database.windows.net:1433;"
                                + "database=AdventureWorks;"
                                + "user=your_username@your_servername;"
                                + "password=your_password;"
                                + "encrypt=true;"
                                + "trustServerCertificate=false;"
                                + "hostNameInCertificate=*.database.windows.net;"
                                + "loginTimeout=30;";


                connection = DriverManager.getConnection(
                        String.format("jdbc:sqlserver://%s;database=%s", host, schema),
                        user,
                        new String(password));

            } else {
                throw new IllegalArgumentException("unknown dialect:" + dialect);
            }

            connection.setAutoCommit(false);
            connection.setReadOnly(readOnly);
            if (readOnly)
                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            return new DbConnection(connection, dialect);
        } catch (SQLException e) {
            System.out.println("cannot connect to server:" + e.getMessage());
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.out.println("cannot find the driver:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void exit(String message,JCommander jCommander) {
        System.out.println(message);
        jCommander.usage();
        System.exit(1);
    }

}
