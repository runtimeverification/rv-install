/* Copyright 2014 Runtime Verification Inc.
 */

package com.izforge.izpack.panels.rvkey;

import java.util.logging.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.*;

import com.izforge.izpack.api.data.InstallData;
import com.izforge.izpack.api.installer.DataValidator;
import com.izforge.izpack.installer.gui.IzPanel;

import java.security.MessageDigest;
import java.math.BigInteger;
import java.io.File;

import com.runtimeverification.licensing.Licensing;
import com.runtimeverification.licensing.RVLicenseCache;

/** Validator to determine whether an RV key is valid, and otherwise not allow install.
 *
 * @author Philip Daian
 */
public class RVKeyOfflineValidator implements DataValidator
{
    private static final Logger logger = Logger.getLogger(IzPanel.class.getName());

    @Override
    public Status validateData(InstallData idata) {
        // Run the RV key algorithm on the IZPack rvKeyEmail, rvKeySecret, and rvProductId variables
        String productId = idata.getVariables().get("rvProductId");
        String fullProductName = idata.getVariables().get("rvFullProductName");
        String licenseToInstall = idata.getVariables().get("rvkey.file");
        File licensePath = new File(new File(idata.getVariables().get("INSTALL_PATH")), idata.getVariables().get("rvLicensePath"));
        licensePath.mkdirs();
        Licensing licensingSystem = new Licensing(licensePath, "predict");
        RVLicenseCache licensingCache = licensingSystem.getLicenseCache();

        // Try to copy given licensing file to final PATH
        try {
            Files.copy((new File(licenseToInstall)).toPath(), (new File(licensePath, "RV_LICENSE")).toPath(), REPLACE_EXISTING);
        }
        catch (Exception ex) {
            return Status.ERROR;
        }

        if (licensingCache.isLicenseCached() && licensingCache.isLicensed()) {
            return Status.OK;
        }

        return Status.ERROR;
    }

    @Override
    public String getErrorMessageId() {
        return "Sorry, license file invalid!  \nPlease check runtimeverification.com/licensing to make sure your product license is active and valid.  \nIf you believe this message is in error, "
                + "please visit https://runtimeverification.com/support.";
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
