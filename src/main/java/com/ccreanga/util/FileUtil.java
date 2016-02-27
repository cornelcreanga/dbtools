package com.ccreanga.util;

import java.io.File;
import java.net.URL;

public class FileUtil {

    public static File locateFile(String file){
        URL url = Thread.currentThread().getContextClassLoader().getResource(file);
        if (url != null)
            return new File(url.getFile());
        else{
            File f = new File(file);
            return f.exists()?f:null;
        }
    }

}
