/* Copyright 2014 Runtime Verification Inc.
 */

package com.izforge.izpack.panels.rvkey;

import java.util.logging.Logger;

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
public class RVKeyValidator implements DataValidator
{
    private static final Logger logger = Logger.getLogger(IzPanel.class.getName());

    @Override
    public Status validateData(InstallData idata) {
        // Run the RV key algorithm on the IZPack rvKeyEmail, rvKeySecret, and rvProductId variables
        String email = idata.getVariables().get("rvKeyEmail").toLowerCase().replaceAll("\\s+", "");
        String password = idata.getVariables().get("rvKeySecret").replaceAll("\\s+", "");
        String productId = idata.getVariables().get("rvProductId");
        String fullProductName = idata.getVariables().get("rvFullProductName");
        File licensePath = new File(new File(idata.getVariables().get("INSTALL_PATH")), idata.getVariables().get("rvLicensePath"));
        licensePath.mkdirs();
        Licensing licensingSystem = new Licensing(licensePath, productId);
        RVLicenseCache licensingCache = licensingSystem.getLicenseCache();

        licensingCache.fetchLatestLicense(email, password);

        if (licensingCache.isLicenseCached() && licensingCache.isLicensed()) {
            return Status.OK;
        }

        return Status.ERROR;
    }

    @Override
    public String getErrorMessageId() {
        return "Sorry, no valid license found for the provided credentials!  \nPlease check runtimeverification.com/licensing to make sure your account details work and your product license is active and valid.  \nIf you believe this message is in error, "
                + "email us at support@runtimeverification.com.";
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
