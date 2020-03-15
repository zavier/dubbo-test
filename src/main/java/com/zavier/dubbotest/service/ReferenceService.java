package com.zavier.dubbotest.service;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.zavier.dubbotest.model.event.UrlEmptyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Reference的服务类
 *
 * @date 2020-01-15 12:43
 * @author zhengwei20
 */
@Slf4j
@Service
public class ReferenceService {

    /**
     * 缓存 The Reference config cache.
     */
    private Cache<String, ReferenceConfig<GenericService>> referenceConfigCache =
            CacheBuilder.newBuilder()
                    .maximumSize(200)
                    .expireAfterWrite(Duration.ofHours(12))
                    .weakValues()
                    .build();

    /**
     * Clear all.
     */
    public void clearAll() {
        referenceConfigCache.invalidateAll();
    }

    /**
     * 获取引用
     *
     * @param providerURL the provider url
     * @return reference config
     */
    public ReferenceConfig<GenericService> getReferenceConfig(URL providerURL) {
        String uniqueKey = providerURL.toFullString();
        if (referenceConfigCache.getIfPresent(uniqueKey) == null) {
            ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
            ApplicationConfig applicationConfig = new ApplicationConfig("dubbo-test");

            // 弱类型接口名
            reference.setApplication(applicationConfig);
            reference.setInterface(providerURL.getServiceInterface());
            reference.setVersion(providerURL.getParameter(Constants.VERSION_KEY));
            // 使用直连
            reference.setUrl(providerURL.toFullString());
            // 声明为泛化接口
            reference.setGeneric(true);
            reference.setTimeout(60000);

            log.info("reference cache add, key:{}", uniqueKey);
            referenceConfigCache.put(uniqueKey, reference);

            return reference;
        }
        return referenceConfigCache.getIfPresent(uniqueKey);
    }


    /**
     * 监听服务的注销，对缓存进行同步修改
     *
     * @param urlEmptyEvent the url empty event
     */
    @EventListener
    public void urlEmptyListener(UrlEmptyEvent urlEmptyEvent) {
        URL url = urlEmptyEvent.getUrl();
        String category = url.getParameter(Constants.CATEGORY_KEY);
        if (StringUtils.isEmpty(category) || !Constants.PROVIDERS_CATEGORY.equals(category)) {
            log.info("listener url:{} is not provider", url.toFullString());
            return;
        }
        // 因为empty协议group和version为*，所以只需要比较interface和ip
        List<String> emptyKeys = referenceConfigCache.asMap().keySet().stream().filter(u -> {
            URL existURL = URL.valueOf(u);
            if (existURL.getServiceInterface().equals(url.getServiceInterface())
                    && existURL.getIp().equals(url.getIp())) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        emptyKeys.forEach(k -> {
            log.info("before cache size:{}", referenceConfigCache.size());
            referenceConfigCache.invalidate(k);
            log.info("after cache size:{}", referenceConfigCache.size());
        });
    }
}
