package com.zavier.dubbotest.common.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The type Coder util.
 *
 * @date 2020-01-15 12:40
 * @author zhengwei20
 */
@Slf4j
public class CoderUtil {

    /**
     * The constant md.
     */
    private static MessageDigest md;
    /**
     * The constant HEX_CODE.
     */
    private static final char[] HEX_CODE = "0123456789ABCDEF".toCharArray();

    static {
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Md 5 16 bit string.
     *
     * @param input the input
     * @return the string
     */
    public static String MD5_16bit(String input) {
        String hash = MD5_32bit(input);
        if (hash == null) {
            return null;
        }
        return hash.substring(8, 24);
    }

    /**
     * Md 5 32 bit string.
     *
     * @param input the input
     * @return the string
     */
    public static String MD5_32bit(String input) {
        if (input == null || input.length() == 0) {
            return null;
        }
        md.update(input.getBytes());
        byte[] digest = md.digest();
        String hash = convertToString(digest);
        return hash;
    }

    /**
     * Md 5 32 bit string.
     *
     * @param input the input
     * @return the string
     */
    public static String MD5_32bit(byte[] input) {
        if (input == null || input.length == 0) {
            return null;
        }
        md.update(input);
        byte[] digest = md.digest();
        String hash = convertToString(digest);
        return hash;
    }

    /**
     * Convert to string string.
     *
     * @param data the data
     * @return the string
     */
    private static String convertToString(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(HEX_CODE[(b >> 4) & 0xF]);
            r.append(HEX_CODE[(b & 0xF)]);
        }
        return r.toString();
    }

}
