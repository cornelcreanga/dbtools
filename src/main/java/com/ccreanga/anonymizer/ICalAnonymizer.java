package com.ccreanga.anonymizer;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

public class ICalAnonymizer implements Anonymizer {

    private int end(StringBuilder s, int start) {
        char[] c = ":;\"".toCharArray();
        int[] indices = new int[c.length];
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < indices.length; i++) {
            indices[i] = s.indexOf("" + c[i], start);
            if (indices[i] == -1)
                indices[i] = Integer.MAX_VALUE;
            if (indices[i] < min)
                min = indices[i];
        }

        if (min == Integer.MAX_VALUE)
            return s.length();
        return min;
    }

    @Override
    public Object anonymize(Object original, List<Object> fullRow) {
        String event = (String) original;
        BufferedReader reader = new BufferedReader(new StringReader(event));
        String line;
        StringBuilder result = new StringBuilder(event.length() + 16);
        StringAnonymizer stringAnonymizer = new StringAnonymizer();
        try {
            while ((line = reader.readLine()) != null) {
                line = line.toUpperCase();
                if (line.startsWith("ORGANIZER") || line.startsWith("ATTENDEE")) {

                    StringBuilder sb = new StringBuilder(line);

                    int start = 0;
                    while (true) {
                        int index = sb.indexOf("CN=", start);
                        if (index == -1)
                            break;
                        if (sb.charAt(index + 3) == '"')
                            index++;
                        int end = end(sb, index + 3);
                        start = end;
                        sb.replace(index + 3, end, stringAnonymizer.anonymize(end - index - 3));
                    }

                    while (true) {
                        int index = sb.indexOf("MAILTO:", start);
                        if (index == -1)
                            break;
                        int end = end(sb, index + 7);
                        start = end;
                        sb.replace(index + 7, end, stringAnonymizer.anonymize(end - index));
                    }
                    result.append(sb).append("\n");

                } else if (line.startsWith("SUMMARY") || line.startsWith("DESCRIPTION")) {//replace after :
                    int index = line.indexOf(":");
                    if (index != -1) {
                        result.append(line.substring(0, index + 1));
                        result.append(stringAnonymizer.anonymize(line.substring(index + 1), Collections.emptyList()));
                        result.append("\n");
                    }

                } else {
                    result.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            System.out.println(event);
            throw new RuntimeException(e);//should never happen
        }
        return result.toString();

    }

}
