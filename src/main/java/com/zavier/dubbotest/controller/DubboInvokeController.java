package com.zavier.dubbotest.controller;

import com.alibaba.fastjson.JSON;
import com.zavier.dubbotest.common.Result;
import com.zavier.dubbotest.model.InvokeMethodParam;
import com.zavier.dubbotest.service.DubboInvokeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Dubbo test controller.
 *
 * @date 2020-01-15 12:40
 * @author zhengwei20
 */
@Slf4j
@RestController
@RequestMapping("api")
public class DubboInvokeController {

    /**
     * The Dubbo invoke service.
     */
    private final DubboInvokeService dubboInvokeService;

    /**
     * Instantiates a new Dubbo test controller.
     *
     * @param dubboInvokeService the dubbo simple test service
     */
    public DubboInvokeController(DubboInvokeService dubboInvokeService) {
        this.dubboInvokeService = dubboInvokeService;
    }

    /**
     * Test object.
     *
     * @param invokeMethodParam the invoke method param
     * @return the object
     */
    @PostMapping("invoke")
    public Object invoke(@RequestBody InvokeMethodParam invokeMethodParam) {
        log.debug("invoke param:{}", JSON.toJSONString(invokeMethodParam));
        try {
            Result result = Result.wrapSuccessfulResult(null);
            Object obj = dubboInvokeService.invokeMethod(invokeMethodParam, result);
            log.info("result:{}", JSON.toJSONString(obj));
            if (!result.isSuccess()) {
                return result;
            }
            return obj;
        } catch (Exception e) {
            log.error("test error", e);
            return Result.wrapErrorResult(e.getMessage());
        }
    }

}
