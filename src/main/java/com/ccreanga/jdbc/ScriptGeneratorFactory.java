package com.ccreanga.jdbc;

import com.ccreanga.jdbc.mysql.MySqlScriptGenerator;
import com.ccreanga.jdbc.oracle.OracleScriptGenerator;
import com.ccreanga.jdbc.postgresql.PostgreSqlScriptGenerator;

public class ScriptGeneratorFactory {

    public static ScriptGenerator createScriptGenerator(Dialect dialect) {
        if (dialect == Dialect.MYSQL)
            return new MySqlScriptGenerator();
        if (dialect == Dialect.POSTGRESQL)
            return new PostgreSqlScriptGenerator();
        if (dialect == Dialect.ORACLE)
            return new OracleScriptGenerator();
        throw new IllegalArgumentException("unknown dialect " + dialect);
    }

}
