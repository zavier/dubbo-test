package com.zavier.dubbotest.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.zavier.dubbotest.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Logger controller.
 *
 * @author zhengwei20
 * @date 2020/02/10
 */
@Slf4j
@RestController
@RequestMapping("loggers")
public class LoggerController {

    /**
     * 修改日志级别
     *
     * @param logLevel the log level
     * @return the result
     */
    @RequestMapping(value = "changeLevel")
    public Result<Boolean> changeLogLevel(@RequestParam("level") String logLevel) {
        try {
            ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
            if (iLoggerFactory instanceof LoggerContext) {
                LoggerContext loggerContext = (LoggerContext) iLoggerFactory;
                loggerContext.getLogger("com.zavier").setLevel(Level.valueOf(logLevel));
            } else {
                return Result.wrapErrorResult("不支持动态修改日志级别");
            }
        } catch (Exception e) {
            log.error("changeLogLevel error", e);
            return Result.wrapErrorResult(e.getMessage());
        }

        return Result.wrapSuccessfulResult(true);
    }

}