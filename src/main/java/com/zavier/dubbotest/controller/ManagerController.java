package com.zavier.dubbotest.controller;

import com.zavier.dubbotest.common.Result;
import com.zavier.dubbotest.common.util.MavenUtil;
import com.zavier.dubbotest.service.ReferenceService;
import com.zavier.dubbotest.service.clientpackage.ClassFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Manager controller.
 *
 * @date 2020-01-15 12:42
 * @author zhengwei20
 */
@Slf4j
@RestController
@RequestMapping("manager")
public class ManagerController {
    /**
     * The Reference service.
     */
    private final ReferenceService referenceService;

    /**
     * The Maven util.
     */
    private final MavenUtil mavenUtil;

    /**
     * The App class factory.
     */
    private final ClassFactory appClassFactory;

    /**
     * Instantiates a new Manager controller.
     *
     * @param referenceService the reference service
     * @param mavenUtil        the maven util
     * @param appClassFactory  the app class factory
     */
    public ManagerController(ReferenceService referenceService,
                             MavenUtil mavenUtil, ClassFactory appClassFactory) {
        this.referenceService = referenceService;
        this.mavenUtil = mavenUtil;
        this.appClassFactory = appClassFactory;
    }


    /**
     * Clear result.
     *
     * @return the result
     */
    @PostMapping("clearReferenceCache")
    public Result clear() {
        referenceService.clearAll();
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * Open force refresh result.
     *
     * @return the result
     */
    @GetMapping("openForceRefresh")
    public Result openForceRefresh() {
        appClassFactory.setForceRefresh(true);
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * Close force refresh result.
     *
     * @return the result
     */
    @GetMapping("closeForceRefresh")
    public Result closeForceRefresh() {
        appClassFactory.setForceRefresh(false);
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * Gets force refresh status.
     *
     * @return the force refresh status
     */
    @GetMapping("getForceRefreshStatus")
    public Result getForceRefreshStatus() {
        boolean forceRefresh = appClassFactory.getForceRefresh();
        return Result.wrapSuccessfulResult(forceRefresh);
    }

    /**
     * Clear all repo result.
     *
     * @return the result
     */
    @GetMapping("clearAllRepo")
    public Result<Boolean> clearAllRepo() {
        boolean b = mavenUtil.clearAll();
        return Result.wrapSuccessfulResult(b);
    }
}
