package com.ccreanga.usecases.export.jdbc.oracle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OracleUtil {

    private static Map<String,String> boundaries = new HashMap<>();

    public static String generateLobBoundary(String identifier){
        String cachedBoundary = boundaries.get(identifier);
        if (cachedBoundary==null){
            String boundary = "--"+ UUID.randomUUID()+"--";
            boundaries.put(identifier,boundary);
            return boundary;
        }
        return cachedBoundary;
    }

    public static String generateLobFileName(String table,String column){
        return table+"#####"+column+".lob";
    }

}
