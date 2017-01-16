package com.ccreanga.usecases.export;

import java.io.Closeable;
import java.util.function.Consumer;

public interface CloseableConsumer<T> extends Consumer<T>, Closeable {
}
