package com.izforge.izpack.panels.process;

import com.izforge.izpack.panels.process.AbstractUIProcessHandler;

import com.izforge.izpack.panels.linux.ProcessRunner;

public class InstallScriptProcessRunner {
    public boolean run(AbstractUIProcessHandler handler, String[] args) {
        String password = args[0];
        String script = args[1];
        String version = args[2];

        boolean success = new ProcessRunner().runWithSudo(
                password, script + " " + version, new HandlerLogger(handler));

        if (success) {
            handler.logOutput("The installation finished successfully.", true);
            handler.finishProcessing(false, true);
            return true;
        }
        handler.logOutput("An error occurred while installing.", true);
        handler.finishProcessing(false, false);
        return false;
    }

    private class HandlerLogger implements ProcessRunner.Logger {
        private final AbstractUIProcessHandler handler;

        private HandlerLogger(AbstractUIProcessHandler handler) {
            this.handler = handler;
        }

        @Override
        public synchronized void log(String message, boolean stderr) {
            handler.logOutput(message, stderr);
        }
    }
}
