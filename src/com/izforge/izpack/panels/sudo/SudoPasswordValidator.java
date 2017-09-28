package com.izforge.izpack.panels.sudo;

import com.izforge.izpack.api.data.InstallData;
import com.izforge.izpack.api.installer.DataValidator;
import com.izforge.izpack.panels.linux.ProcessRunner;
import com.izforge.izpack.installer.gui.IzPanel;

public class SudoPasswordValidator implements DataValidator {
    @Override
    public Status validateData(InstallData idata) {
        // Run the RV key algorithm on the IZPack rvKeyEmail, rvKeySecret, and rvProductId variables
        String password = idata.getVariables().get("sudoPassword");

        if (new ProcessRunner().runWithSudo(password, "ls", ProcessRunner.NULL_LOGGER)) {
            return Status.OK;
        }

        return Status.ERROR;
    }

    @Override
    public String getErrorMessageId() {
        return "Either the password is wrong, or you cannot run sudo.";
    }

    @Override
    public String getWarningMessageId() {
        return null;
    }

    @Override
    public boolean getDefaultAnswer() {
        return false;
    }

}
