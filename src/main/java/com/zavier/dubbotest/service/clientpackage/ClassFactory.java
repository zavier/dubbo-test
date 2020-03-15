package com.zavier.dubbotest.service.clientpackage;

import com.zavier.dubbotest.common.util.MavenUtil;
import com.zavier.dubbotest.model.ClassDetail;
import com.zavier.dubbotest.service.JarClassLoaderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Jar包中的类信息工厂
 *
 * @date 2020-01-15 12:40
 * @author zhengwei20
 */
@Slf4j
@Component
public class ClassFactory {

    /**
     * The Maven util.
     */
    private final MavenUtil mavenUtil;

    /**
     * The Jar class loader service.
     */
    private final JarClassLoaderService jarClassLoaderService;

    /**
     * Instantiates a new App class factory.
     *
     * @param mavenUtil             the maven util
     * @param jarClassLoaderService the jar class loader service
     */
    public ClassFactory(MavenUtil mavenUtil, JarClassLoaderService jarClassLoaderService) {
        this.mavenUtil = mavenUtil;
        this.jarClassLoaderService = jarClassLoaderService;
    }

    /**
     * key groupId:artifactId:version
     */
    private ConcurrentHashMap<String, Map<String, ClassDetail>> appClassMap = new ConcurrentHashMap<>();

    /**
     * Gets class map.
     *
     * @param packageInfo the package info
     * @return the class map
     */
    public Map<String, ClassDetail> getClassMap(PackageInfo packageInfo) {
        Map<String, ClassDetail> classMap = appClassMap.get(packageInfo.getCoords());
        if (classMap == null) {
            reloadIfNecessary(packageInfo);
        }
        return appClassMap.get(packageInfo.getCoords());
    }

    /**
     * The Force refresh.
     */
    private boolean forceRefresh = true;

    /**
     * Sets force refresh.
     *
     * @param forceRefresh the force refresh
     */
    public void setForceRefresh(boolean forceRefresh) {
        this.forceRefresh = forceRefresh;
    }

    /**
     * Gets force refresh.
     *
     * @return the force refresh
     */
    public boolean getForceRefresh() {
        return forceRefresh;
    }

    /**
     * 重载jar包中的类信息
     *
     * @param packageInfo the package info
     */
    public void reloadIfNecessary(PackageInfo packageInfo) {
        if (packageInfo == null) {
            log.info("no fit packageInfo");
            return;
        }
        // 下载最新版本的包
        String path = mavenUtil.download(packageInfo, forceRefresh);

        if (path != null) {
            log.info("path:{}", path);
            // 获取下载的所有jar包
            Collection collection = FileUtils.listFiles(new File(path), new String[]{"jar"}, true);
            Iterator iterator = collection.iterator();
            List<String> jarNameList = new ArrayList<>();
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (next instanceof File) {
                    File jarName = (File) next;
                    jarNameList.add(jarName.getAbsolutePath());
                }
            }
            Map<String, ClassDetail> classMap = jarClassLoaderService.loadAllClass(jarNameList);
            if (!CollectionUtils.isEmpty(classMap)) {
                String appKey = packageInfo.getCoords();
                appClassMap.put(appKey, classMap);
            }
        }
    }

}
