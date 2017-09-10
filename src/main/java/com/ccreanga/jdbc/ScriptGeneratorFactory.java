package com.ccreanga.jdbc;

import com.ccreanga.jdbc.mysql.MySqlScriptGenerator;
import com.ccreanga.jdbc.oracle.OracleScriptGenerator;
import com.ccreanga.jdbc.postgresql.PostgreSqlScriptGenerator;

public class ScriptGeneratorFactory {

    public static ScriptGenerator createScriptGenerator(Dialect dialect,String folderName,boolean override) {
        if (dialect == Dialect.MYSQL)
            return new MySqlScriptGenerator(folderName,override);
        if (dialect == Dialect.POSTGRESQL)
            return new PostgreSqlScriptGenerator(folderName,override);
//        if (dialect == Dialect.ORACLE)
//            return new OracleScriptGenerator(folderName,override);
        throw new IllegalArgumentException("unknown dialect " + dialect);
    }

}
