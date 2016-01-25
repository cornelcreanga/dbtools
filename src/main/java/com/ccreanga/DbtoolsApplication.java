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
public class DbtoolsApplication {


    public static void main(String[] args) throws ParseException {

        DbConnection dbConnection = null;
        String[] authArgs = null;
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(120);
        char[] password=null;

        Option springAnsiEnabled = Option.builder("s").longOpt("spring.output.ansi.enabled").hasArgs().build();

        Option authenticateOption = Option.builder("a")
                .valueSeparator(' ')
                .numberOfArgs(3)
                .argName("url> <schema> <user")
                .longOpt("auth")
                .desc("Authentication data: server url, user.")
                .build();

        Option exportOption = Option.builder("e")
                .valueSeparator(' ')
                .numberOfArgs(4)
                .argName("pattern>  <folder> <overwrite> <anonymize")
                .longOpt("export")
                .desc("Exports the table matching the specified pattern. ")
                .build();


        Options options = new Options();
        options.addOption(authenticateOption);
        options.addOption(exportOption);
        options.addOption(springAnsiEnabled);

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse( options, args,false);

        if (!cmd.hasOption("a")){
            System.out.println("authentication is mandatory");
            formatter.printHelp( "dbtools", options );
            System.exit(1);
        }else{
            authArgs = cmd.getOptionValues("a");
            if ((authArgs==null) || (authArgs.length!=3)){
                System.out.println("authentication parameters are mandatory");
                formatter.printHelp( "dbtools", options );
                System.exit(1);
            }
            if (false) {
                try {
                    password = readPassword("[%s]", "Password:");
                } catch (IOException e) {
                    System.exit(1);
                }
            }
            password = "root".toCharArray();//todo
            //todo - for the moment only MYSQL
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("cannot load mysql driver");
                System.exit(1);
            }

            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://"+authArgs[0]+"/"+authArgs[1]+"?user="+authArgs[2]+"&password="+new String(password)+"&zeroDateTimeBehavior=convertToNull");
                dbConnection = new DbConnection(connection, Dialect.MYSQL);
            } catch (SQLException e) {
                System.out.println("cannot connect to mysql server:"+e.getMessage());
                System.exit(1);
            }

        }

        if (cmd.hasOption("e")){
            String[] exportArgs = cmd.getOptionValues("e");
            if ((exportArgs==null) || (exportArgs.length!=4)){
                System.out.println("export parameters are mandatory");
                formatter.printHelp( "dbtools", options );
                System.exit(1);
            }
            String schema = authArgs[1];
            String pattern = exportArgs[0];
            String folder = exportArgs[1];
            String overwrite = exportArgs[2];
            String anonymizationRules = exportArgs[3];
            DataAnonymizer dataAnonymizer = null;


            if (anonymizationRules!=null){
                dataAnonymizer = new DataAnonymizer(anonymizationRules);
            }

            MySqlTablesExport mySqlTablesExport = dataAnonymizer==null?
                    new MySqlTablesExport():
                    new MySqlTablesExport(dataAnonymizer);

            long t1= System.currentTimeMillis();
            mySqlTablesExport.exportTables(dbConnection,new Schema(schema),pattern,folder,overwrite.equalsIgnoreCase("y"));
            long t2= System.currentTimeMillis();
            System.out.println((t2-t1)/1000);
        }



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
