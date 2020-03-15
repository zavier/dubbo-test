package com.zavier.dubbotest.service;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.zavier.dubbotest.model.InvokeMethodParam;
import com.zavier.dubbotest.service.registrysync.RegistryServerSync;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 服务提供者 ProviderService
 *
 * @date 2020-01-15 12:43
 * @author zhengwei20
 */
@Service
public class ProviderService {

    /**
     * The Sync.
     */
    private final RegistryServerSync sync;

    /**
     * Instantiates a new Provider service.
     *
     * @param sync the sync
     */
    public ProviderService(RegistryServerSync sync) {
        this.sync = sync;
    }

    /**
     * Find service map.
     *
     * @return the map
     */
    public Map<String, Map<String, URL>> findService() {
        ConcurrentMap<String, ConcurrentMap<String, Map<String, URL>>> registryCache = sync.getRegistryCache();
        ConcurrentMap<String, Map<String, URL>> serviceMap = registryCache.get(Constants.PROVIDERS_CATEGORY);
        return serviceMap;
    }

    /**
     * 通过参数获取具体的url
     *
     * @param invokeMethodParam the invoke method param
     * @return url url
     */
    public URL findProviderURL(InvokeMethodParam invokeMethodParam) {
        ConcurrentMap<String, Map<String, URL>> providerMap = sync.getRegistryCache().get(Constants.PROVIDERS_CATEGORY);
        if (providerMap == null || providerMap.size() == 0) {
            return null;
        }
        Set<Map.Entry<String, Map<String, URL>>> entries = providerMap.entrySet();
        for (Map.Entry<String, Map<String, URL>> entry : entries) {
            String service = entry.getKey();
            if (Objects.equals(service, invokeMethodParam.getServiceKey())) {
                Map<String, URL> idMap = entry.getValue();
                URL url = idMap.get(invokeMethodParam.getId());
                if (url != null) {
                    return url;
                } else {
                    return idMap.values().iterator().next();
                }
            }
        }
        return null;
    }
}
