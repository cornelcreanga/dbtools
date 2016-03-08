package com.ccreanga.jdbc;

public class ScriptGeneratorFactory {

    public static ScriptGenerator createScriptGenerator(Dialect dialect) {
        if (dialect == Dialect.MYSQL)
            return new MySqlScriptGenerator();
        if (dialect == Dialect.POSTGRESQL)
            return new PostgreSqlScriptGenerator();
        throw new IllegalArgumentException("uknown dialect " + dialect);
    }

}
