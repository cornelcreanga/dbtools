package com.ccreanga.anonymizer;

import com.ccreanga.random.Language;
import com.ccreanga.random.RandomNameGenerator;
import com.ccreanga.random.RandomNameGeneratorFactory;

public class EmailAnonymizer implements Anonymizer {

    RandomNameGenerator randomNameGenerator = RandomNameGeneratorFactory.generator(Language.FANTASY);

    @Override
    public Object anonymize(Object original) {
        return randomNameGenerator.compose(2)+"."+randomNameGenerator.compose(2)+"@domain.com";
    }

}
