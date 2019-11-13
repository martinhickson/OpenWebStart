package com.openwebstart.os;

import com.openwebstart.install4j.Install4JUtils;
import com.openwebstart.jvm.os.OperationSystem;
import net.adoptopenjdk.icedteaweb.logging.Logger;
import net.adoptopenjdk.icedteaweb.logging.LoggerFactory;
import net.sourceforge.jnlp.JNLPFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.Executors;

public class ScriptFactory {

    private final static Logger LOG = LoggerFactory.getLogger(ScriptFactory.class);


    private final static String SCRIPT_START = "#!/bin/sh";

    private static final String JAVA_WS_NAME = "javaws";

    public static String createStartScript(final JNLPFile jnlpFile) {
        if(OperationSystem.getLocalSystem() == OperationSystem.MAC64) {
            return createStartScriptForMac(jnlpFile);
        }
        final String executable = Install4JUtils.installationDirectory()
                .map(d -> d + "/" + JAVA_WS_NAME)
                .orElseThrow(() -> new IllegalStateException("Can not define executable"));
        return executable + " \"" + Optional.ofNullable(jnlpFile.getSourceLocation()).orElse(jnlpFile.getFileLocation()) + "\"";
    }

    public static String createStartScriptForMac(final JNLPFile jnlpFile) {
        final String executable = "\"" + Install4JUtils.installationDirectory()
                .map(d -> d + "/" + "OpenWebStart javaws.app" + "\"")
                .orElseThrow(() -> new IllegalStateException("Can not define executable"));

        final String scriptContent = SCRIPT_START + System.lineSeparator() + "open -a " + executable;

        return scriptContent + " \"" + jnlpFile.getFileLocation() + "\"";
    }

    public static Process createStartProcess(final JNLPFile jnlpFile) throws IOException {
        final String executable = "\"" + Install4JUtils.installationDirectory()
                .map(d -> d + "/" + "OpenWebStart javaws.app" + "\"")
                .orElseThrow(() -> new IllegalStateException("Can not define executable"));
        final String fileLocation = "\"" + jnlpFile.getFileLocation() + "\"";
        final ProcessBuilder builder = new ProcessBuilder();
        builder.command("open", "-a", executable, fileLocation);
        builder.redirectErrorStream(true);
        final Process process = builder.start();
        logIO(process.getInputStream());
        return process;
    }

    private static void logIO(final InputStream src) {
        Executors.newSingleThreadExecutor().execute(() -> {
            final Scanner sc = new Scanner(src);
            while (sc.hasNextLine()) {
                LOG.debug("APP: " + sc.nextLine());
            }
        });
    }
}
