package com.zavier.dubbotest.service.login;

import com.zavier.dubbotest.common.BusinessException;
import com.zavier.dubbotest.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * The type Login service.
 *
 * @date 2020-01-15 12:42
 * @author zhengwei20
 */
@Slf4j
@Service
public class LoginService {

    /**
     * The Password service.
     */
    private final PasswordService passwordService;

    /**
     * The Token service.
     */
    private final TokenService tokenService;

    /**
     * Instantiates a new Login service.
     *
     * @param passwordService the password service
     * @param tokenService    the token service
     */
    public LoginService(PasswordService passwordService,
                        TokenService tokenService) {
        this.passwordService = passwordService;
        this.tokenService = tokenService;
    }

    /**
     * Is login boolean.
     *
     * @param authorizationHeader the authorization header
     * @return the boolean
     */
    public Boolean isLogin(String authorizationHeader) {
        if (StringUtils.isBlank(authorizationHeader)) {
            return false;
        }
        String token = tokenService.getTokenFromHeader(authorizationHeader);
        return tokenService.checkToken(token);
    }

    /**
     * Sign in string.
     *
     * @param user the user
     * @return the string
     */
    public String signIn(User user) {
        if (user == null || !user.check()) {
            throw new BusinessException("用户名或密码不能为空");
        }

        String username = user.getUserName();
        String encryptedPwd = passwordService.getEncryptedPwd(username);
        if (StringUtils.isBlank(encryptedPwd)) {
            throw new BusinessException("用户名或密码错误");
        }
        String encryptPassword = passwordService.encryptPassword(user.getPassword());
        log.info("expected:{}, actual:{}", encryptPassword, encryptedPwd);
        if (Objects.equals(encryptPassword, encryptedPwd)) {
            return tokenService.generateToken(username);
        } else {
            throw new BusinessException("用户名或密码错误");
        }
    }

}
