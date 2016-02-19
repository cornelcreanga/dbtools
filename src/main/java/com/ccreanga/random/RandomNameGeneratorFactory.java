package com.ccreanga.random;

import java.io.IOException;
import java.net.URL;

public class RandomNameGeneratorFactory {

    private static RandomNameGenerator fantasy;

    static{
        try {
            fantasy = new RandomNameGenerator(loadResource("fantasy.txt").getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static RandomNameGenerator generator(Language language){
        if (language==Language.FANTASY){
            return fantasy;
        }else{
            throw new IllegalArgumentException("language not yet implemented:"+language);
        }
    }

    private static URL loadResource(String resource){
        URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
        if (url==null)
            throw new RuntimeException("can't locate the file "+resource+" in the classpath");
        return url;
    }


}
