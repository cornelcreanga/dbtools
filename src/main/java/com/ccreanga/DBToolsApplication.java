package com.ccreanga;

import com.ccreanga.jdbc.Dialect;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.usecases.export.DataAnonymizer;
import com.ccreanga.usecases.process.SqlTablesAnonymizer;
import com.ccreanga.usecases.export.SqlTablesExport;
import com.ccreanga.util.ConsoleUtil;
import com.ccreanga.util.FormatUtil;
import org.apache.commons.cli.*;

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

    public static void main(String[] args) throws ParseException {

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(120);
        String host = null, user = null, schema = null;
        char[] password = null;

        Option hostOption = Option.builder(HOST)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("host")
                .longOpt(HOST)
                .desc("Host (server:port)")
                .build();
        Option dialectOption = Option.builder(DIALECT)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("Dialect (MYSQL or POSTGRESQL)")
                .longOpt(DIALECT)
                .desc("Dialect (MYSQL or POSTGRESQL)")
                .build();

        Option userOption = Option.builder(USER)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("username")
                .longOpt(USER)
                .desc("User")
                .build();
        Option passwdOption = Option.builder(PASSWORD)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("password")
                .longOpt(PASSWORD)
                .desc("Password")
                .build();
        Option schemaOption = Option.builder(SCHEMA)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("schema")
                .longOpt(SCHEMA)
                .desc("Schema")
                .build();

        Option exportOption = Option.builder(EXPORT)
                .valueSeparator(' ')
                .numberOfArgs(3)
                .argName("pattern>  <folder> <overwrite")
                .longOpt(EXPORT)
                .desc("Exports the table(s) matching the specified pattern. It expects three params: table pattern, destination folder , overwrite(y/n). If anonymization parameters are" +
                        " specified it will export anonymized values")
                .build();

        Option anonymizeOption = Option.builder(AN)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("anonymization_file_rules> <force")
                .longOpt(AN)
                .desc("Anonymize data using the specified rules. When used with the export parameters it will generate export files, otherwise it will apply the changes directly into the database." +
                        "If <force> is not configured to yes it will double check if you really want to do that")
                .build();

        Options options = new Options();
        options.addOption(hostOption);
        options.addOption(dialectOption);
        options.addOption(userOption);
        options.addOption(passwdOption);
        options.addOption(schemaOption);
        options.addOption(exportOption);
        options.addOption(anonymizeOption);

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args, false);
        } catch (UnrecognizedOptionException e) {
            exit(e.getMessage(),formatter,options);
        }

        Dialect dialect = Dialect.MYSQL;

        if (cmd.hasOption(DIALECT)) {
            try {
                dialect = Dialect.valueOf(cmd.getOptionValue(DIALECT));
            } catch (IllegalArgumentException e) {
                exit("Don't understand dialect " + cmd.getOptionValue(DIALECT),formatter,options);
            }
        } else {
            System.out.println("No dialect specified - using by default " + dialect);
        }

        if (!cmd.hasOption(HOST)) {
            exit("host is mandatory",formatter,options);
        } else {
            host = cmd.getOptionValue(HOST);
        }
        if (!cmd.hasOption(USER)) {
            exit("user is mandatory",formatter,options);
        } else {
            user = cmd.getOptionValue(USER);
        }

        if (!cmd.hasOption(SCHEMA)) {
            exit("schema is mandatory",formatter,options);
        } else {
            schema = cmd.getOptionValue(SCHEMA);
        }
        if (!cmd.hasOption(PASSWORD)) {
            password = ConsoleUtil.readPassword("[%s]", "Password:");
        } else {
            password = cmd.getOptionValue(PASSWORD).toCharArray();
        }

        //anonymize database
        if (cmd.hasOption(AN) && !cmd.hasOption(EXPORT)) {
            try (
                    DbConnection readConnection = connection(dialect, host, schema, user, password, true);
                    DbConnection writeConnection = connection(dialect, host, schema, user, password, false);
            ) {
                anonymizeDatabase(readConnection, writeConnection, schema, new DataAnonymizer(cmd.getOptionValue(AN)));
            } catch (Exception e) {
                System.out.println("An exception occured during the anonymization process, the full stracktrace is");
                e.printStackTrace();
            }
            return;
        }

        //generate export files and optionally anonymize them
        if (cmd.hasOption(EXPORT)) {
            DataAnonymizer dataAnonymizer = null;
            if (cmd.hasOption(AN)) {
                dataAnonymizer = new DataAnonymizer(cmd.getOptionValue(AN));
            }
            String[] export = cmd.getOptionValues(EXPORT);

            try (DbConnection connection = connection(dialect, host, schema, user, password, true)) {
                exportDatabase(connection, schema, export[0], export[1], export[2].equalsIgnoreCase("y"), dataAnonymizer);
            } catch (Exception e) {
                System.out.println("An exception occured during the export process, the full stracktrace is");
                e.printStackTrace();
            }
        }

    }

    private static void anonymizeDatabase(DbConnection readConnection, DbConnection writeConnection, String schema, DataAnonymizer dataAnonymizer) {
        SqlTablesAnonymizer sqlTablesAnonymizer = new SqlTablesAnonymizer(dataAnonymizer);

        long t1 = System.currentTimeMillis();
        sqlTablesAnonymizer.anonymizeTables(readConnection, writeConnection, new Schema(schema));
        long t2 = System.currentTimeMillis();
        System.out.println("Anonymization finished in " + FormatUtil.formatMillis(t2-t1) + " seconds.");

    }

    private static void exportDatabase(DbConnection connection, String schema, String pattern, String folder, boolean overwrite, DataAnonymizer dataAnonymizer) {
        SqlTablesExport sqlTablesExport = dataAnonymizer == null ?
                new SqlTablesExport() :
                new SqlTablesExport(dataAnonymizer);

        long t1 = System.currentTimeMillis();
        sqlTablesExport.exportTables(connection, new Schema(schema), pattern, folder, overwrite);
        long t2 = System.currentTimeMillis();
        System.out.println("Export finished in " + FormatUtil.formatMillis(t2-t1) + " seconds.");
    }

    private static DbConnection connection(Dialect dialect, String host, String schema, String user, char[] password, boolean readOnly) {
        try {
            Connection connection;
            if (dialect == Dialect.MYSQL) {
                connection = DriverManager.getConnection(
                        String.format("jdbc:mysql://%s/%s?user=%s&password=%s&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=%s",
                                host,schema,user,new String(password),MySqlConfig.rewriteBatchedStatements));
            } else if (dialect == Dialect.POSTGRESQL) {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(
                        String.format("jdbc:postgresql://%s/%s?user=%s&password=%s",host,schema,user,new String(password)));
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

    private static void exit(String message,HelpFormatter formatter,Options options){
        System.out.println(message);
        formatter.printHelp("dbtools", options);
        System.exit(1);
    }

}
