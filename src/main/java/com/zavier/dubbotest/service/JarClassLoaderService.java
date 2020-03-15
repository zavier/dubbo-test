package com.zavier.dubbotest.service;

import com.zavier.dubbotest.model.ClassDetail;
import javassist.bytecode.ClassFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 加载解析jar包
 *
 * @date 2020-01-15 12:41
 * @author zhengwei20
 */
@Slf4j
@Component
public class JarClassLoaderService {

    /**
     * 获取所有jar包中的类，并注册
     *
     * @param jarPaths the jar paths
     * @return map map
     */
    public Map<String, ClassDetail> loadAllClass(List<String> jarPaths) {
        if (CollectionUtils.isEmpty(jarPaths)) {
            return new HashMap<>(2);
        }

        Map<String, ClassDetail> classMap = new HashMap<>(100);
        for (String jarPath : jarPaths) {
            Map<String, ClassDetail> classDetailMap = loadClasses(jarPath);
            classMap.putAll(classDetailMap);
        }
        return classMap;
    }

    /**
     * 解析jar包中的类信息
     *
     * @param jarPath the jar path
     * @return Map className, byte[]
     */
    private Map<String, ClassDetail> loadClasses(String jarPath) {
        Map<String, ClassDetail> classDetailMap = new HashMap<>(2);
        if (StringUtils.isBlank(jarPath)) {
            return classDetailMap;
        }
        try {
            JarFile jarFile = new JarFile(jarPath);
            Enumeration enu = jarFile.entries();
            while (enu.hasMoreElements()) {
                Object o = enu.nextElement();
                if (o instanceof JarEntry) {
                    JarEntry jarEntry = (JarEntry) o;
                    String jarEntryName = jarEntry.getName();
                    if (jarEntryName.endsWith(".class")) {
                        int index = jarEntryName.lastIndexOf(".");
                        String className = jarEntryName.replace('/', '.').substring(0, index);
                        log.debug("load className:{}", className);
                        InputStream inputStream = jarFile.getInputStream(jarEntry);

                        ClassDetail classDetail = new ClassDetail();
                        classDetail.setClassFile(new ClassFile(new DataInputStream(inputStream)));
                        classDetailMap.put(className, classDetail);
                    }
                }
            }
        } catch (Exception e) {
            log.error("load jar:{} class error", jarPath, e);
        }
        return classDetailMap;
    }

}
