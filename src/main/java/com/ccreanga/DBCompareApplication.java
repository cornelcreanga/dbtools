package com.ccreanga;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DBCompareApplication {

    public static final String HOST1 = "host1";
    public static final String USER1 = "user1";
    public static final String PASSWORD1 = "password1";
    public static final String SCHEMA1 = "schema1";
    public static final String TABLE1 = "table1";

    public static final String HOST2 = "host2";
    public static final String USER2 = "user2";
    public static final String PASSWORD2 = "password2";
    public static final String SCHEMA2 = "schema2";
    public static final String TABLE2 = "table2";


    public static void main(String[] args) throws ParseException {
        Option hostOption1 = Option.builder(HOST1)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("host")
                .longOpt(HOST1)
                .desc("Host (server:port)")
                .build();
        Option userOption1 = Option.builder(USER1)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("username")
                .longOpt(USER1)
                .desc("User")
                .build();
        Option passwdOption1 = Option.builder(PASSWORD1)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("password")
                .longOpt(PASSWORD1)
                .desc("Password")
                .build();
        Option schemaOption1 = Option.builder(SCHEMA1)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("schema")
                .longOpt(SCHEMA1)
                .desc("Schema")
                .build();
        Option table1 = Option.builder(TABLE1)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("table (optional)")
                .longOpt(TABLE1)
                .desc("Table")
                .build();


        Option hostOption2 = Option.builder(HOST2)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("host")
                .longOpt(HOST2)
                .desc("Host (server:port)")
                .build();
        Option userOption2 = Option.builder(USER2)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("username")
                .longOpt(USER2)
                .desc("User")
                .build();
        Option passwdOption2 = Option.builder(PASSWORD2)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("password")
                .longOpt(PASSWORD2)
                .desc("Password")
                .build();
        Option schemaOption2 = Option.builder(SCHEMA2)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("schema")
                .longOpt(SCHEMA2)
                .desc("Schema")
                .build();
        Option table2 = Option.builder(TABLE2)
                .valueSeparator(' ')
                .numberOfArgs(1)
                .argName("table (optional)")
                .longOpt(TABLE2)
                .desc("Table")
                .build();
    }

}
