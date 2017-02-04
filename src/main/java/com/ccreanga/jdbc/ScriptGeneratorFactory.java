package com.ccreanga.jdbc;

import com.ccreanga.jdbc.mysql.MySqlScriptGenerator;
import com.ccreanga.jdbc.oracle.OracleScriptGenerator;
import com.ccreanga.jdbc.postgresql.PostgreSqlScriptGenerator;

public class ScriptGeneratorFactory {

    public static ScriptGenerator createScriptGenerator(Dialect dialect,String folderName) {
        if (dialect == Dialect.MYSQL)
            return new MySqlScriptGenerator(folderName);
        if (dialect == Dialect.POSTGRESQL)
            return new PostgreSqlScriptGenerator(folderName);
        if (dialect == Dialect.ORACLE)
            return new OracleScriptGenerator(folderName);
        throw new IllegalArgumentException("unknown dialect " + dialect);
    }

}
