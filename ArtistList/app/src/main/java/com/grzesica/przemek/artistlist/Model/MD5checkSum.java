package com.grzesica.przemek.artistlist.Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.inject.Named;

public class MD5checkSum implements IMD5checkSum{

    private Appendable mStringBuffer;

    @Inject
    public MD5checkSum(@Named("stringBuffer") Appendable stringBuffer){
        this.mStringBuffer = stringBuffer;
    }

    @Override
    public String stringToMD5(String jsonString) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(jsonString.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = (StringBuffer)mStringBuffer;
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
