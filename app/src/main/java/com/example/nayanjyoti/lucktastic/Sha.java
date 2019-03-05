package com.example.nayanjyoti.lucktastic;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 ** http://lugeek.com
 */

public class Sha {
    /**
     *encrypt method,SHA-1,SHA-224,SHA-256,SHA-384,SHA-512
     * @param data to be encrypted
     * @return 已加密的数据
     * @throws Exception
     */
    public static String encryptSHA(String data) throws Exception {
        String shaN = "SHA-256";
        byte[] inputByte = data.getBytes();
        MessageDigest sha = MessageDigest.getInstance(shaN);
        sha.update(inputByte);
        return sha.digest().toString();
    }

    public static String decryptSHA(String encryptText) throws Exception {
        byte[] outputData = encryptText.getBytes();
        BigInteger decrypt = new BigInteger(1,outputData);
        return decrypt.toString();
    }
}
