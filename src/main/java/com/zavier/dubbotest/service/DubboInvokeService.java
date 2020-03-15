package com.zavier.dubbotest.service;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.zavier.dubbotest.common.Result;
import com.zavier.dubbotest.model.InvokeMethodParam;
import com.zavier.dubbotest.service.apiparam.ApiParamInfo;
import com.zavier.dubbotest.service.apiparam.ParamService;
import com.zavier.dubbotest.service.apiparam.ParamServiceFactory;
import com.zavier.dubbotest.service.registrysync.RegistryServerSync;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Dubbo测试服务
 *
 * @date 2020-01-15 12:40
 * @author zhengwei20
 */
@Slf4j
@Service
public class DubboInvokeService {

    /**
     * The Provider service.
     */
    private final ProviderService providerService;


    /**
     * The Reference service.
     */
    private final ReferenceService referenceService;

    /**
     * The Param service factory.
     */
    private final ParamServiceFactory paramServiceFactory;

    /**
     * The constant DEFAULT_VERSION.
     */
    public static final String DEFAULT_VERSION = "test-SNAPSHOT";

    /**
     * Instantiates a new Dubbo simple test service.
     *
     * @param sync                the sync
     * @param providerService     the provider service
     * @param referenceService    the reference service
     * @param paramServiceFactory the param service factory
     */
    public DubboInvokeService(RegistryServerSync sync,
                              ProviderService providerService,
                              ReferenceService referenceService,
                              ParamServiceFactory paramServiceFactory) {
        this.providerService = providerService;
        this.referenceService = referenceService;
        this.paramServiceFactory = paramServiceFactory;
    }

    /**
     * invoke method
     *
     * @param invokeMethodParam the invoke method param
     * @param result            the result
     * @return the object
     */
    public Object invokeMethod(InvokeMethodParam invokeMethodParam, Result result) {
        // 获取URL
        URL providerURL = providerService.findProviderURL(invokeMethodParam);
        if (providerURL == null) {
            result.setSuccess(false);
            result.setMessage("此服务不存在");
            return null;
        }

        // 生成注册的URL对应的 reference
        ReferenceConfig<GenericService> reference = referenceService.getReferenceConfig(providerURL);
        GenericService genericService = reference.get();

        // 获取参数信息
        ParamService paramService = paramServiceFactory.getParamService(invokeMethodParam);
        ApiParamInfo apiParamInfo = paramService.getApiParamInfo(invokeMethodParam);

        log.debug("apiParamInfo:{}", JSON.toJSONString(apiParamInfo));

        // 进行泛化调用
        return genericService.$invoke(invokeMethodParam.getMethod(),
                apiParamInfo.getParamTypeList().toArray(new String[0]),
                apiParamInfo.getParamDataList().toArray(new Object[0]));
    }
}
