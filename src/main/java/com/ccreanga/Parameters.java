package com.ccreanga;

import com.beust.jcommander.Parameter;

public class Parameters {

    @Parameter(names = {"-h", "--host"}, description = "Host (server:port)", required = true)
    private String host;
    @Parameter(names = {"-u", "--user"}, description = "User", required = true)
    private String user;
    @Parameter(names = {"-p", "--password"}, description = "Password")
    private String password ;
    @Parameter(names = {"-s", "--schema"}, description = "Schema", required = true)
    private String schema ;
    @Parameter(names = {"-d", "--dialect"}, description = "Dialect (MYSQL/POSTGRESQL/ORACLE/CASSANDRA)", required = true)
    private String dialect;

    @Parameter(names = {"-i", "--info"}, description = "<pattern>")
    private String info;
    @Parameter(names = {"-a", "--anonymize"}, description = "<anonymization_file_rules> <force> Anonymize data using the specified rules. When used with the export parameters it will generate export files, otherwise it will apply the changes directly into the database." +
            "If <force> is not configured to yes it will double check if you really want to do that")
    private String anonymize;
    @Parameter(names = {"-e", "--export"}, description = "<pattern>  <folder> <overwrite> Exports the table(s) matching the specified pattern. It expects three params: table pattern, destination folder , overwrite(y/n). If anonymization parameters are specified it will export anonymized values")
    private String export;

    @Parameter(names = "--help", help = true)
    public boolean help = false;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getAnonymize() {
        return anonymize;
    }

    public void setAnonymize(String anonymize) {
        this.anonymize = anonymize;
    }

    public String getExport() {
        return export;
    }

    public void setExport(String export) {
        this.export = export;
    }
}
