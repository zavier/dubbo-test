package com.zavier.dubbotest.config;

import com.zavier.dubbotest.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Mvc config.
 *
 * @date 2020-01-15 12:42
 * @author zhengwei20
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * The Login interceptor.
     */
    private final LoginInterceptor loginInterceptor;

    /**
     * Instantiates a new Mvc config.
     *
     * @param loginInterceptor the login interceptor
     */
    public MvcConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/api/**")
                .addPathPatterns("/method/**")
                .addPathPatterns("/manager/**");
    }

}
