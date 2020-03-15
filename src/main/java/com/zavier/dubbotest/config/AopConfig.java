package com.zavier.dubbotest.config;

import com.zavier.dubbotest.common.BusinessException;
import com.zavier.dubbotest.common.PagingResult;
import com.zavier.dubbotest.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

/**
 * The type Aop config.
 *
 * @date 2020-01-15 12:39
 * @author zhengwei20
 */
@Slf4j
@Configuration
@Aspect
public class AopConfig {

    /**
     * Exception wrapper.
     */
    @Pointcut("execution(* com.zavier.dubbotest.controller..*.*(..))")
    public void exceptionWrapper(){}

    /**
     * Around object.
     *
     * @param joinPoint the join point
     * @return the object
     */
    @Around("exceptionWrapper()")
    public Object around(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();

        } catch (BusinessException e) {
            // 业务异常，自己记录日志，此处不打印
            Signature signature = joinPoint.getSignature();
            if (signature instanceof MethodSignature) {
                Class returnType = ((MethodSignature) signature).getReturnType();
                if (returnType == PagingResult.class) {
                    return PagingResult.wrapErrorResult(e.getMessage());
                }
            }
            return Result.wrapErrorResult(e.getMessage());
        } catch (Throwable throwable) {
            log.error("execute error", throwable);
            String msg = "系统繁忙，请稍后再试";
            Signature signature = joinPoint.getSignature();
            if (signature instanceof MethodSignature) {
                Class returnType = ((MethodSignature) signature).getReturnType();
                if (returnType == PagingResult.class) {
                    return PagingResult.wrapErrorResult(msg);
                }
            }
            return Result.wrapErrorResult(msg);
        }
    }
}
