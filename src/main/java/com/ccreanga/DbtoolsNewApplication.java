package com.ccreanga;

import com.ccreanga.jdbc.Dialect;
import com.ccreanga.jdbc.model.DbConnection;
import com.ccreanga.jdbc.model.Schema;
import com.ccreanga.usecases.export.DataAnonymizer;
import com.ccreanga.usecases.export.MySqlTablesExport;
import org.apache.commons.cli.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class DbtoolsNewApplication {


    public static void main(String[] args) throws ParseException {

        DbConnection dbConnection = null;
        String[] authArgs = null;
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(120);
        char[] password=null;

        Option springAnsiEnabled = Option.builder("s").longOpt("spring.output.ansi.enabled").hasArgs().build();

        Option host = Option.builder("h")
                .valueSeparator('=')
                .numberOfArgs(1)
                .argName("host")
                .longOpt("host")
                .desc("Host (server:port)")
                .build();
        Option user = Option.builder("u")
                .valueSeparator('=')
                .numberOfArgs(1)
                .argName("user")
                .longOpt("user")
                .desc("User")
                .build();
        Option passwd = Option.builder("p")
                .valueSeparator('=')
                .numberOfArgs(1)
                .argName("password")
                .longOpt("password")
                .desc("Password")
                .build();
        Option schema = Option.builder("s")
                .valueSeparator('=')
                .numberOfArgs(1)
                .argName("schema")
                .longOpt("schema")
                .desc("Schema")
                .build();

        Option exportOption = Option.builder("e")
                .valueSeparator(' ')
                .numberOfArgs(3)
                .argName("pattern>  <folder> <overwrite")
                .longOpt("export")
                .desc("Exports the table(s) matching the specified pattern. It expects three params: table pattern, destination folder , overwrite(yes/no). If anonymization parameters are" +
                        "specified it will export anonymized values")
                .build();

        Option anonymizeOption = Option.builder("an")
                .valueSeparator(' ')
                .numberOfArgs(2)
                .argName("anonymization_file_rules> <force")
                .longOpt("an")
                .desc("Anonymize data using the specified rules. When used with the export parameters it will generate export files, otherwise it will apply the changes directly into the database." +
                        "If <force> is not configured to yes it will double check if you really want to do that")
                .build();

        Options options = new Options();
        options.addOption(host);
        options.addOption(user);
        options.addOption(passwd);
        options.addOption(schema);
        options.addOption(exportOption);
        options.addOption(anonymizeOption);
        options.addOption(springAnsiEnabled);

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args,false);

        if (!cmd.hasOption("h")){
            System.out.println("host is mandatory");
            formatter.printHelp( "dbtools", options );
            System.exit(1);
        }
        if (!cmd.hasOption("u")){
            System.out.println("user is mandatory");
            formatter.printHelp( "dbtools", options );
            System.exit(1);
        }
        if (!cmd.hasOption("s")){
            System.out.println("schema is mandatory");
            formatter.printHelp( "dbtools", options );
            System.exit(1);
        }
        if (!cmd.hasOption("p")){
            try {
                password = readPassword("[%s]", "Password:");
            } catch (IOException e) {
                System.exit(1);
            }
        }

        if (cmd.hasOption("an") && !cmd.hasOption("e")){
            String[] anonymize = cmd.getOptionValues("an");
            anonymizeDatabase();
            return;
        }

        if (cmd.hasOption("e")){
            String[] anonymize = null;
            if (cmd.hasOption("an")){
                anonymize = cmd.getOptionValues("an");
            }
            String[] export = cmd.getOptionValues("e");
            exportDatabase();
            return;
        }




//        if (!cmd.hasOption("a")){
//            System.out.println("authentication is mandatory");
//            formatter.printHelp( "dbtools", options );
//            System.exit(1);
//        }else{
//            authArgs = cmd.getOptionValues("a");
//            if ((authArgs==null) || (authArgs.length!=3)){
//                System.out.println("authentication parameters are mandatory");
//                formatter.printHelp( "dbtools", options );
//                System.exit(1);
//            }
//            if (false) {
//                try {
//                    password = readPassword("[%s]", "Password:");
//                } catch (IOException e) {
//                    System.exit(1);
//                }
//            }
//            password = "c0rn3lic@".toCharArray();//todo
//            //todo - for the moment only MYSQL
//            try {
//                Class.forName("com.mysql.jdbc.Driver");
//            } catch (ClassNotFoundException e) {
//                System.out.println("cannot load mysql driver");
//                System.exit(1);
//            }
//
//            try {
//                Connection connection = DriverManager.getConnection("jdbc:mysql://"+authArgs[0]+"/"+authArgs[1]+"?user="+authArgs[2]+"&password="+new String(password)+"&zeroDateTimeBehavior=convertToNull");
//                dbConnection = new DbConnection(connection, Dialect.MYSQL);
//            } catch (SQLException e) {
//                System.out.println("cannot connect to mysql server:"+e.getMessage());
//                System.exit(1);
//            }
//
//        }
//
//        if (cmd.hasOption("e")){
//            String[] exportArgs = cmd.getOptionValues("e");
//            if ((exportArgs==null) || (exportArgs.length!=3)){
//                System.out.println("export parameters are mandatory");
//                formatter.printHelp( "dbtools", options );
//                System.exit(1);
//            }
//            String schema = authArgs[1];
//            String pattern = exportArgs[0];
//            String folder = exportArgs[1];
//            String overwrite = exportArgs[2];
//
//            DataAnonymizer dataAnonymizer = null;
//            if (cmd.hasOption("an")) {
//
//                String anonymizationRules = exportArgs[0];
//                if (anonymizationRules != null) {
//                    dataAnonymizer = new DataAnonymizer(anonymizationRules);
//                }
//            }
//
//            MySqlTablesExport mySqlTablesExport = dataAnonymizer==null?
//                    new MySqlTablesExport():
//                    new MySqlTablesExport(dataAnonymizer);
//
//            long t1= System.currentTimeMillis();
//            mySqlTablesExport.exportTables(dbConnection,new Schema(schema),pattern,folder,overwrite.equalsIgnoreCase("y"));
//            long t2= System.currentTimeMillis();
//            System.out.println((t2-t1)/1000);
//        }

    }

    private static void anonymizeDatabase(){

    }

    private static void exportDatabase(){

    }

    private static String readLine(String format, Object... args) throws IOException {
        if (System.console() != null) {
            return System.console().readLine(format, args);
        }
        System.out.print(String.format(format, args));
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    private static char[] readPassword(String format, Object... args)
            throws IOException {
        if (System.console() != null)
            return System.console().readPassword(format, args);
        return readLine(format, args).toCharArray();
    }
}
