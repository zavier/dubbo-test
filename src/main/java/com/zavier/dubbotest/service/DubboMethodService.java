package com.zavier.dubbotest.service;

import com.zavier.dubbotest.common.BusinessException;
import com.zavier.dubbotest.model.ClassDetail;
import com.zavier.dubbotest.model.ClassMethod;
import com.zavier.dubbotest.service.clientpackage.ClassFactory;
import com.zavier.dubbotest.service.clientpackage.ClientPackageRegistry;
import com.zavier.dubbotest.service.clientpackage.PackageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * The type Dubbo method service.
 *
 * @date 2020-01-15 12:41
 * @author zhengwei20
 */
@Slf4j
@Service
public class DubboMethodService {

    /**
     * The Class factory.
     */
    private final ClassFactory classFactory;

    /**
     * The Client package registry.
     */
    private final ClientPackageRegistry clientPackageRegistry;

    /**
     * Instantiates a new Dubbo method service.
     *
     * @param classFactory          the class factory
     * @param clientPackageRegistry the client package registry
     */
    public DubboMethodService(ClassFactory classFactory,
                              ClientPackageRegistry clientPackageRegistry) {
        this.classFactory = classFactory;
        this.clientPackageRegistry = clientPackageRegistry;
    }

    /**
     * List methods info list.
     *
     * @param serviceInterface the service interface
     * @param version          the version
     * @return the list
     */
    public List<ClassMethod> listMethodsInfo(String serviceInterface, String version) {
        PackageInfo packageInfo = clientPackageRegistry.guessPackageInfoByServiceInterface(serviceInterface, version);
        if (packageInfo == null) {
            throw new BusinessException("包信息不存在");
        }
        Map<String, ClassDetail> classMap = classFactory.getClassMap(packageInfo);
        if (classMap == null) {
            throw new BusinessException("包对应的类信息不存在");
        }

        ClassDetail serviceClass = classMap.get(serviceInterface);
        if (serviceClass == null) {
            throw new BusinessException("类信息不存在");
        }

        List<ClassMethod> methods = serviceClass.getMethods();
        return methods;
    }
}
