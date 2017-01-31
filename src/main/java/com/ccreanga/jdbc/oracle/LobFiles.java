package com.ccreanga.jdbc.oracle;

import com.ccreanga.usecases.export.jdbc.oracle.OracleUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class LobFiles {

    private static Map<String,OutputStream> lobs = new HashMap<>();

    public static OutputStream getOutputStream(String table,String column){
        return lobs.get(OracleUtil.generateLobFileName(table,column));
    }

    public static void put(String key,OutputStream out){
        lobs.put(key,out);
    }

    public static void closeLobForTable(String table){
        lobs.forEach((k,v)->{
            try {
                if (k.startsWith(table))
                    v.close();
            } catch (IOException e) {
            }
        });
    }

}
