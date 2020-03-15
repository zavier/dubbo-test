package com.zavier.dubbotest.service.clientpackage;

import com.google.common.base.Splitter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * api-jar包信息
 *
 * @date 2020-01-15 12:40
 * @author zhengwei20
 */
@Component
public class ClientPackageRegistry {
    /**
     * The Client version map.
     */
    private ConcurrentHashMap<String, PackageInfo> clientVersionMap = new ConcurrentHashMap<>();

    /**
     * The Alias name map.
     */
    private ConcurrentHashMap<String, String> aliasNameMap = new ConcurrentHashMap<>();

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * 获取所有包信息
     *
     * @return package map
     */
    public Map<String, PackageInfo> getPackageMap() {
        return Collections.unmodifiableMap(clientVersionMap);
    }

    /**
     * 通过项目名获取对应包信息
     *
     * @param clientName the client name
     * @param version    the version
     * @return package info
     */
    public PackageInfo getPackageInfoByClientName(String clientName, String version) {
        PackageInfo packageInfo = clientVersionMap.get(clientName);
        if (packageInfo != null) {
            packageInfo.setVersion(version);
            return packageInfo;
        }

        String name = aliasNameMap.get(clientName);
        if (name != null) {
            packageInfo = clientVersionMap.get(name);
            packageInfo.setVersion(version);
            return packageInfo;
        }

        return null;
    }

    /**
     * 尽力通过服务接口和版本获取包信息（不一定准确）
     *
     * @param serviceInterface the service interface
     * @param version          the version
     * @return package info
     */
    public PackageInfo guessPackageInfoByServiceInterface(String serviceInterface, String version) {
        PackageInfo special = findSpecial(serviceInterface, version);
        if (special != null) {
            return special;
        }

        // guess app
        List<String> packageInfos = Splitter.on(".").splitToList(serviceInterface);
        // 一般包名中第三个为项目名, 前3个为groupId
        int projectIndex = 2;
        if (packageInfos.size() >= projectIndex) {
            String guessClientName = packageInfos.get(2);
            String groupId = String.join(".", packageInfos.get(0), packageInfos.get(1), guessClientName);
            // 根据groupId查找
            Optional<PackageInfo> optionalPackageInfo = clientVersionMap.values()
                    .stream()
                    .filter(pack -> groupId.equals(pack.getGroupId()))
                    .findFirst();
            if (optionalPackageInfo.isPresent()) {
                PackageInfo packageInfo = optionalPackageInfo.get();
                packageInfo.setVersion(version);
                return packageInfo;
            }

            // 根据猜测的包名查找
            PackageInfo packageInfo = getPackageInfoByClientName(guessClientName, version);
            if (packageInfo != null) {
                packageInfo.setVersion(version);
                return packageInfo;
            }

            // 猜测具体包名
            String artifactId = guessClientName + "-client";
            return new PackageInfo(groupId, artifactId, version);
        }

        return null;
    }

    /**
     * 特殊处理的名字
     * @param serviceInterface
     * @param version
     * @return
     */
    private PackageInfo findSpecial(String serviceInterface, String version) {
        String perceptor = "perceptor";
        if (serviceInterface.contains(perceptor)) {
            PackageInfo packageInfo = clientVersionMap.get(perceptor);
            packageInfo.setVersion(version);
            return packageInfo;
        }
        return null;
    }

}
