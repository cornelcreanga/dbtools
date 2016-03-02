package com.ccreanga;

import com.ccreanga.jdbc.Dialect;
import com.ccreanga.util.FileUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public class Config {

    Map<String,DataSource> dataSources;

    private static Config config;

    public Map<String, DataSource> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Map<String, DataSource> dataSources) {
        this.dataSources = dataSources;
    }

    public DataSource getDataSource(Dialect dialect){
         return dataSources.get(dialect.name().toLowerCase());
    }

    static{
        InputStream in = FileUtil.classPathResource("application.yml");
        Yaml yaml = new Yaml();
        config = yaml.loadAs( in, Config.class );
    }

    public static Config getConfig() {
        return config;
    }

    public static void main(String[] args) {

    }
}