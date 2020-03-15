package com.zavier.dubbotest.controller;

import com.zavier.dubbotest.common.Constants;
import com.zavier.dubbotest.common.Result;
import com.zavier.dubbotest.model.User;
import com.zavier.dubbotest.service.login.LoginService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * The type Login controller.
 *
 * @date 2020-01-15 12:41
 * @author zhengwei20
 */
@RestController
@RequestMapping("login")
public class LoginController {

    /**
     * The Login service.
     */
    private final LoginService loginService;

    /**
     * Instantiates a new Login controller.
     *
     * @param loginService the login service
     */
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Is login result.
     *
     * @param request the request
     * @return the result
     */
    @GetMapping("isLogin")
    public Result<Boolean> isLogin(HttpServletRequest request) {
        String authorization = request.getHeader(Constants.LOGIN_HEADER);
        Boolean login = loginService.isLogin(authorization);
        return Result.wrapSuccessfulResult(login);
    }

    /**
     * Sign in result.
     *
     * @param user the user
     * @return the result
     */
    @PostMapping("signIn")
    public Result<String> signIn(@RequestBody User user) {
        String s = loginService.signIn(user);
        return Result.wrapSuccessfulResult(s);
    }

}
