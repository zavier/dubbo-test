package com.zavier.dubbotest.interceptor;

import com.alibaba.fastjson.JSON;
import com.zavier.dubbotest.common.Constants;
import com.zavier.dubbotest.common.Result;
import com.zavier.dubbotest.service.login.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * The type Login interceptor.
 *
 * @date 2020-01-15 12:42
 * @author zhengwei20
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * The Token service.
     */
    private final TokenService tokenService;

    /**
     * Instantiates a new Login interceptor.
     *
     * @param tokenService the token service
     */
    public LoginInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String header = request.getHeader(Constants.LOGIN_HEADER);
        if (!isLogin(header)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            Result<Object> result = Result.wrapErrorResult("请先登录");
            writer.write(JSON.toJSONString(result));
            writer.flush();
            return false;
        }
        return true;
    }

    /**
     * Is login boolean.
     *
     * @param authHeader the auth header
     * @return the boolean
     */
    private boolean isLogin(String authHeader) {
        if (StringUtils.isBlank(authHeader)) {
            return false;
        }

        String token = tokenService.getTokenFromHeader(authHeader);
        return tokenService.checkToken(token);
    }
}
