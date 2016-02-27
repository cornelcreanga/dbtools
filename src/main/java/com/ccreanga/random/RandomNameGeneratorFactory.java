package com.ccreanga.random;

import com.ccreanga.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class RandomNameGeneratorFactory {

    private static RandomNameGenerator fantasy;

    static {
        try {
            File grammar = FileUtil.locateFile("fantasy.txt");
            if (grammar==null)
                throw new RuntimeException("can't locate the file fantasy.txt in the classpath");
            fantasy = new RandomNameGenerator(grammar);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static RandomNameGenerator generator(Language language) {
        if (language == Language.FANTASY) {
            return fantasy;
        } else {
            throw new IllegalArgumentException("language not yet implemented:" + language);
        }
    }

}
