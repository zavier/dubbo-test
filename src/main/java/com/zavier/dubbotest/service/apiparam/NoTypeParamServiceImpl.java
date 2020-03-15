package com.zavier.dubbotest.service.apiparam;

import com.alibaba.dubbo.common.URL;
import com.alibaba.fastjson.JSON;
import com.zavier.dubbotest.common.BusinessException;
import com.zavier.dubbotest.model.ClassDetail;
import com.zavier.dubbotest.model.ClassMethod;
import com.zavier.dubbotest.model.InvokeMethodParam;
import com.zavier.dubbotest.model.ParamPair;
import com.zavier.dubbotest.service.ProviderService;
import com.zavier.dubbotest.service.clientpackage.ClassFactory;
import com.zavier.dubbotest.service.clientpackage.ClientPackageRegistry;
import com.zavier.dubbotest.service.clientpackage.PackageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户未指定参数类型（需要根据app和版本号 从jar包中自行获取)
 *
 * @author zhengwei20
 * @date 2020 -01-08 19:29
 */
@Slf4j
@Service
public class NoTypeParamServiceImpl implements ParamService {

    /**
     * The Client package registry.
     */
    private final ClientPackageRegistry clientPackageRegistry;

    /**
     * The App class factory.
     */
    private final ClassFactory appClassFactory;

    /**
     * The Provider service.
     */
    private final ProviderService providerService;

    /**
     * The constant DEFAULT_VERSION.
     */
    @Value("${default.version}")
    private String defaultVersion;

    /**
     * Instantiates a new No type param service.
     *
     * @param clientPackageRegistry the client package registry
     * @param appClassFactory       the app class factory
     * @param providerService       the provider service
     */
    public NoTypeParamServiceImpl(ClientPackageRegistry clientPackageRegistry,
                                  ClassFactory appClassFactory,
                                  ProviderService providerService) {
        this.clientPackageRegistry = clientPackageRegistry;
        this.appClassFactory = appClassFactory;
        this.providerService = providerService;
    }


    @Override
    public ApiParamInfo getApiParamInfo(InvokeMethodParam invokeMethodParam) {
        List<String> paramTypes = new ArrayList<>();
        List<Object> paramObjs = new ArrayList<>();
        List<Object> jsonArrayParams = invokeMethodParam.getJsonArrayParams();

        List<String> paramTypeList = getParamInfoByPackageInfo(invokeMethodParam);
        for (int i = 0; i < paramTypeList.size(); i++) {
            String paramType = paramTypeList.get(i);
            paramTypes.add(paramType);
            if (!paramType.startsWith("java.")) {
                Map map = JSON.parseObject(JSON.toJSONString(jsonArrayParams.get(i)), Map.class);
                paramObjs.add(map);
            } else {
                paramObjs.add(jsonArrayParams.get(i));
            }
        }
        return new ApiParamInfo(paramTypes, paramObjs);
    }


    /**
     * 获取参数类型信息（通过下载jar包方式）
     *
     * @param invokeMethodParam the invoke method param
     * @return param type list by package info
     */
    private List<String> getParamInfoByPackageInfo(InvokeMethodParam invokeMethodParam) {
        // 确保app信息完整
        URL providerURL = providerService.findProviderURL(invokeMethodParam);

        PackageInfo packageInfo = null;
        if (invokeMethodParam.hasAppInfo()) {
            packageInfo = clientPackageRegistry.getPackageInfoByClientName(invokeMethodParam.getApp(), invokeMethodParam.getAppVersion());
        } else {
            String appVersion = StringUtils.isBlank(invokeMethodParam.getAppVersion()) ? defaultVersion : invokeMethodParam.getAppVersion();
            packageInfo = clientPackageRegistry.guessPackageInfoByServiceInterface(providerURL.getServiceInterface(), appVersion);
        }

        if (packageInfo == null) {
            throw new BusinessException("包信息不存在");
        }

        Map<String, ClassDetail> classMap = appClassFactory.getClassMap(packageInfo);
        if (classMap == null) {
            throw new BusinessException("无此app: " + packageInfo.getCoords());
        }
        // todo 后续如果服务分组，此处需要改动
        String service = invokeMethodParam.getServiceKey().split(":")[0];
        ClassDetail serviceClass = classMap.get(service);
        if (serviceClass == null) {
            throw new BusinessException("无此服务：" + invokeMethodParam.getServiceKey());
        }

        List<ClassMethod> methods = serviceClass.getMethods();
        for (ClassMethod m : methods) {
            String methodName = m.getMethodName();
            if (Objects.equals(methodName, invokeMethodParam.getMethod())) {
                List<ParamPair> paramInfoList = m.getParamInfoList();
                return paramInfoList.stream().map(ParamPair::getParamType).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

}
