package com.openwebstart.util;

import net.adoptopenjdk.icedteaweb.logging.Logger;
import net.adoptopenjdk.icedteaweb.logging.LoggerFactory;

import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProcessUtil {

    private final static Logger LOG = LoggerFactory.getLogger(ProcessUtil.class);

    public static void logIO(final InputStream src) {
        Executors.newSingleThreadExecutor().execute(() -> {
            final Scanner sc = new Scanner(src);
            while (sc.hasNextLine()) {
                LOG.debug("APP: " + sc.nextLine());
            }
        });
    }

    public static Future<String> getIO(final InputStream src) {
        final CompletableFuture<String> result = new CompletableFuture<>();
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                final StringBuilder builder = new StringBuilder();
                final Scanner sc = new Scanner(src);
                while (sc.hasNextLine()) {
                    builder.append(sc.nextLine());
                    if (sc.hasNextLine()) {
                        builder.append(System.lineSeparator());
                    }
                }
                result.complete(builder.toString());
            } catch (final Exception e) {
                result.completeExceptionally(e);
            }
        });
        return result;
    }
}
