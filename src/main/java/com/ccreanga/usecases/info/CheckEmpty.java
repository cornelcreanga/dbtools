package com.ccreanga.usecases.info;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class CheckEmpty implements Consumer<List<Object>>, Closeable {

    private int[] nonEmpty;

    public CheckEmpty(int size) {
        nonEmpty = new int[size];
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void accept(List<Object> objects) {
        for (int i = 0; i < objects.size(); i++) {
            Object o = objects.get(i);
            if (o != null) {
                if (o instanceof String) {
                    if (((String) o).trim().length() > 0)
                        nonEmpty[i]++;
                } else
                    nonEmpty[i]++;
            }
        }
    }

    public int[] getNonEmpty() {
        return nonEmpty;
    }
}
