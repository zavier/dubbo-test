package com.zavier.dubbotest.controller;

import com.zavier.dubbotest.common.Result;
import com.zavier.dubbotest.model.ClassMethod;
import com.zavier.dubbotest.service.DubboMethodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The type Dubbo method controller.
 *
 * @date 2020-01-15 12:41
 * @author zhengwei20
 */
@Slf4j
@RestController
@RequestMapping("method")
public class DubboMethodController {

    /**
     * The Dubbo method service.
     */
    private final DubboMethodService dubboMethodService;

    /**
     * Instantiates a new Dubbo method controller.
     *
     * @param dubboMethodService the dubbo method service
     */
    public DubboMethodController(DubboMethodService dubboMethodService) {
        this.dubboMethodService = dubboMethodService;
    }

    /**
     * List methods info result.
     *
     * @param serviceInterface the service interface
     * @param version          the version
     * @return the result
     */
    @GetMapping("listMethodsInfo")
    public Result<List<ClassMethod>> listMethodsInfo(@RequestParam("serviceInterface") String serviceInterface,
                                                     @RequestParam(defaultValue = "test-SNAPSHOT") String version) {
        List<ClassMethod> serviceMethodDTOList = dubboMethodService.listMethodsInfo(serviceInterface, version);
        return Result.wrapSuccessfulResult(serviceMethodDTOList);
    }
}
