package com.ccreanga.anonymizer;

import java.util.List;

public interface Anonymizer {

    Object anonymize(Object original, List<Object> fullRow);
}
