package com.ccreanga.random;

import com.ccreanga.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;

public class RandomNameGeneratorFactory {

    private static RandomNameGenerator fantasy;

    static {
        try (InputStream in = FileUtil.classPathResource("fantasy.txt")) {
            if (in == null)
                throw new RuntimeException("can't locate the file fantasy.txt in the classpath");
            fantasy = new RandomNameGenerator(in);
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
