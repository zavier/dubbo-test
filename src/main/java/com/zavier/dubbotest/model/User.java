package com.zavier.dubbotest.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * The type User.
 *
 * @date 2020-01-15 12:44
 * @author zhengwei20
 */
@Data
public class User {
    /**
     * The User name.
     */
    private String userName;

    /**
     * The Password.
     */
    private String password;

    /**
     * Check boolean.
     *
     * @return the boolean
     */
    public boolean check() {
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            return false;
        }
        return true;
    }
}
