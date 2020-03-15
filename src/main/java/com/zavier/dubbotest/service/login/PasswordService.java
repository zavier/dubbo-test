package com.zavier.dubbotest.service.login;

import org.apache.commons.codec.digest.Sha2Crypt;
import org.apache.commons.lang3.StringUtils;

/**
 * The interface Password service.
 *
 * @date 2020-01-15 12:43
 * @author zhengwei20
 */
public interface PasswordService {

    /**
     * Encrypt password string.
     *
     * @param password the password
     * @return the string
     */
    default String encryptPassword(String password) {
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException();
        }
        return  Sha2Crypt.sha512Crypt(password.getBytes(), "$6$" + StringUtils.reverse(password));
    }

    /**
     * Gets encrypted pwd.
     *
     * @param userName the user name
     * @return the encrypted pwd
     */
    String getEncryptedPwd(String userName);
}
