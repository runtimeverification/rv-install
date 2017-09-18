package com.izforge.izpack.panels.linux;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.FunctionalInterface;
import java.util.ArrayList;
import java.util.List;

public class ProcessRunner {
    public static final Logger NULL_LOGGER = new Logger() {
        @Override
        public void log(String message, boolean stderr) {}
    };
    private volatile boolean finished = false;

    @FunctionalInterface
    public interface Logger {
        /**
         * Logs a message from a process' stdout or stderr. Should be thread-safe.
         *
         * @param message The message to log.
         * @param stderr Whether the message originated from the process' stderr.
         */
        void log(String message, boolean stderr);
    }

    /**
     * Runs a command as root. The command is run using /bin/sh -c.
     *
     * Note that this method sends the password to the (sudo+script) combination, so, if the
     * sudo command will not ask for a password, then the given command will receive it.
     *
     * @param password The sudo password.
     * @param command The command to run.
     * @param logger Object to receive the program output. Should be thread-safe.
     * @return true for success, false otherwise.
     */
    public boolean runWithSudo(String password, String command, Logger logger) {

        List<Thread> threads = new ArrayList<>();

        try {
            String[] fullCommand = new String[] {
                    "/bin/sh", "-c", "sudo --stdin " + command
            };
            Process p = Runtime.getRuntime().exec(fullCommand);
            finished = false;

            threads.add(new Thread(new PasswordInput(p.getOutputStream(), password)));
            threads.add(new Thread(new OutputCopier(p.getInputStream(), logger, false)));
            threads.add(new Thread(new OutputCopier(p.getErrorStream(), logger, true)));

            for (Thread t : threads) {
                t.start();
            }
            if (p.waitFor() == 0) {
                finishThreads(threads);
                return true;
            }
        } catch (IOException | InterruptedException e) {
            logger.log(e.toString(), true);
            e.printStackTrace();
        }
        try {
            finishThreads(threads);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void finishThreads(List<Thread> threads) throws InterruptedException {
        finished = true;
        for (Thread t : threads) {
            t.join();
        }
    }

    private class OutputCopier implements Runnable {
        private final Logger logger;
        private final InputStream inputStream;
        private final boolean isStderr;

        private OutputCopier(InputStream inputStream, Logger logger, boolean isStderr) {
            this.inputStream = inputStream;
            this.logger = logger;
            this.isStderr = isStderr;
        }

        @Override
        public void run() {
            try (BufferedReader inErr = new BufferedReader(new InputStreamReader(inputStream))) {
                while (!finished) {
                    String line = inErr.readLine();
                    if (line == null) {
                        break;
                    }
                    logger.log(line, isStderr);
                }
            } catch (Throwable ignored) {
            }
        }
    }

    private class PasswordInput implements Runnable {
        private final OutputStream outputStream;
        private final String password;

        private PasswordInput(OutputStream outputStream, String password) {
            this.outputStream = outputStream;
            this.password = password;
        }

        @Override
        public void run() {
            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream))) {
                out.write(password);
                out.write("\n");
                out.flush();
            } catch (Throwable ignored) {
            }
        }
    }
}
