/* Copyright 2014 Runtime Verification Inc.
 */

package com.izforge.izpack.panels.rvkey;

import java.util.logging.Logger;

import com.izforge.izpack.api.data.InstallData;
import com.izforge.izpack.api.installer.DataValidator;
import com.izforge.izpack.installer.gui.IzPanel;

import java.security.MessageDigest;
import java.math.BigInteger;

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
        String key = idata.getVariables().get("rvKeySecret").replaceAll("\\s+", "");
        String productId = idata.getVariables().get("rvProductId");
        String fullProductName = idata.getVariables().get("rvFullProductName");
        String keyBase = email + "runtimeverification_licensees_only" + productId;
        try {
            MessageDigest m=MessageDigest.getInstance("MD5");
            m.update(keyBase.getBytes(),0,keyBase.length());
            String hashOutput = new BigInteger(1,m.digest()).toString(16);
            // Pad MD5 with 0's that may have gotten pruned by integer representation
            while ( hashOutput.length() < 32 ) {
                hashOutput = "0" + hashOutput;
            }
            String expectedString = fullProductName + "#" + hashOutput.substring(0, 10);
            if (expectedString.equals(key))
                return Status.OK;
            return Status.ERROR;
        }
        catch (Exception e) {
            e.printStackTrace();
            return Status.OK;
        }
    }

    @Override
    public String getErrorMessageId() {
        return "Sorry, your key is incorrect!  Please generate a new key.  \nIf you believe this message is in error, "
                + "email us at contact@runtimeverification.com.";
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
