package com.ccreanga.anonymizer;

import java.util.List;
import java.util.UUID;

public class UUIDAnonymizer implements Anonymizer {
    @Override
    public Object anonymize(Object original,List<Object> fullRow) {
        return UUID.randomUUID();
    }
}
