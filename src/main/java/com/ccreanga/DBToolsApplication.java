package com.ccreanga;

import com.ccreanga.jdbc.Dialect;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.usecases.export.DataAnonymizer;
import com.ccreanga.usecases.export.MySqlTablesAnonymizer;
import com.ccreanga.usecases.export.MySqlTablesExport;
import com.ccreanga.util.ConsoleUtil;
import org.apache.commons.cli.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
  -host 127.0.0.1:3306 -schema test -user root -password root -export ^test.*  /tmp/exp y -an /home/cornel/projects/dbtools/src/main/resources/an.yml
 *
 */

@SpringBootApplication
public class DBToolsApplication {


    public static final String HOST = "host";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String SCHEMA = "schema";
    public static final String EXPORT = "export";
    public static final String AN = "an";

    public static void main(String[] args) throws ParseException {

        DbConnection dbConnection = null;
        String[] authArgs = null;
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(120);
        String host=null,user=null,schema=null;
        char[] password = null;

        Option springAnsiEnabled = Option.builder("s").longOpt("spring.output.ansi.enabled").hasArgs().build();

        Option hostOption = Option.builder(HOST)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("host")
                .longOpt(HOST)
                .desc("Host (server:port)")
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
                .desc("Exports the table(s) matching the specified pattern. It expects three params: table pattern, destination folder , overwrite(yes/no). If anonymization parameters are" +
                        "specified it will export anonymized values")
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
        options.addOption(userOption);
        options.addOption(passwdOption);
        options.addOption(schemaOption);
        options.addOption(exportOption);
        options.addOption(anonymizeOption);
        options.addOption(springAnsiEnabled);

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        if (!cmd.hasOption(HOST)) {
            System.out.println("host is mandatory");
            formatter.printHelp("dbtools", options);
            System.exit(1);
        }else{
            host = cmd.getOptionValue(HOST);
        }
        if (!cmd.hasOption(USER)) {
            System.out.println("user is mandatory");
            formatter.printHelp("dbtools", options);
            System.exit(1);
        }else{
            user = cmd.getOptionValue(USER);
        }

        if (!cmd.hasOption(SCHEMA)) {
            System.out.println("schema is mandatory");
            formatter.printHelp("dbtools", options);
            System.exit(1);
        }else{
            schema = cmd.getOptionValue(SCHEMA);
        }
        if (!cmd.hasOption(PASSWORD)) {
            password = ConsoleUtil.readPassword("[%s]", "Password:");
        } else {
            password = cmd.getOptionValue(PASSWORD).toCharArray();
        }

        //anonymize database
        if (cmd.hasOption(AN) && !cmd.hasOption(EXPORT)) {
            DbConnection connection1 = connection(host,schema,user,password);
            DbConnection connection2 = connection(host,schema,user,password);
            anonymizeDatabase(connection1,connection2,schema,new DataAnonymizer(cmd.getOptionValue(AN)));
            return;
        }

        //generate export files and optionally anonymize them
        if (cmd.hasOption(EXPORT)) {
            DataAnonymizer dataAnonymizer = null;
            if (cmd.hasOption(AN)) {
                dataAnonymizer = new DataAnonymizer(cmd.getOptionValue(AN));
            }
            String[] export = cmd.getOptionValues(EXPORT);
            DbConnection connection =connection(host,schema,user,password);

            exportDatabase(connection,schema,export[0],export[1],export[2].equalsIgnoreCase("y"),dataAnonymizer);
        }

    }

    private static void anonymizeDatabase(DbConnection readConnection,DbConnection writeConnection,String schema, DataAnonymizer dataAnonymizer) {
        MySqlTablesAnonymizer mySqlTablesAnonymizer = new MySqlTablesAnonymizer(dataAnonymizer);

        long t1 = System.currentTimeMillis();
        mySqlTablesAnonymizer.anonymizeTables(readConnection,writeConnection,new Schema(schema));
        long t2 = System.currentTimeMillis();
        System.out.println((t2 - t1) / 1000);

    }

    private static void exportDatabase(DbConnection connection,String schema,String pattern,String folder, boolean overwrite, DataAnonymizer dataAnonymizer) {
        MySqlTablesExport mySqlTablesExport = dataAnonymizer == null ?
                new MySqlTablesExport() :
                new MySqlTablesExport(dataAnonymizer);

        long t1 = System.currentTimeMillis();
        mySqlTablesExport.exportTables(connection, new Schema(schema), pattern, folder, overwrite);
        long t2 = System.currentTimeMillis();
        System.out.println((t2 - t1) / 1000);
    }

    private static DbConnection connection(String host, String schema, String user, char[] password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + schema + "?user=" + user + "&password=" + new String(password) + "&zeroDateTimeBehavior=convertToNull");
            connection.setAutoCommit(false);
            return new DbConnection(connection, Dialect.MYSQL);
        } catch (SQLException e) {
            System.out.println("cannot connect to mysql server:" + e.getMessage());
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            System.out.println("cannot find mysql driver:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }


}
