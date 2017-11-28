/* Copyright 2014 Runtime Verification Inc.
 */

package com.izforge.izpack.panels.rvkey;


import com.izforge.izpack.api.data.InstallData;
import com.izforge.izpack.api.installer.DataValidator;
import com.izforge.izpack.installer.gui.IzPanel;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.logging.Logger;

import javax.net.ssl.SSLEngineResult.Status;

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
        // @rv: We stop checking license
        return Status.OK;

        /*
        // Run the RV key algorithm on the IZPack rvKeyEmail, rvKeySecret, and rvProductId variables
        String email = idata.getVariables().get("rvKeyEmail").toLowerCase().replaceAll("\\s+", "");
        String password = idata.getVariables().get("rvKeySecret").replaceAll("\\s+", "");
        String productId = idata.getVariables().get("rvProductId");
        String fullProductName = idata.getVariables().get("rvFullProductName");
        Licensing licensingSystem = Licensing.fromLocations(
                productId,
                Licensing.LicenseLocation.USER_DIRECTORY,
                Collections.emptyList());
        RVLicenseCache licensingCache = licensingSystem.getLicenseCache();

        licensingCache.fetchLatestLicense(email, password);

        if (licensingCache.isLicenseCached() && licensingCache.isLicensed()) {
            return Status.OK;
        }

        return Status.ERROR;
        */
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
