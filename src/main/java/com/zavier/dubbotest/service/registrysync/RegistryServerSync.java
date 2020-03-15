package com.zavier.dubbotest.service.registrysync;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.registry.NotifyListener;
import com.alibaba.dubbo.registry.Registry;
import com.zavier.dubbotest.common.util.CoderUtil;
import com.zavier.dubbotest.common.util.Tool;
import com.zavier.dubbotest.model.event.UrlEmptyEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 注册中心相关同步器
 *
 * @date 2020-01-15 12:43
 * @author zhengwei20
 */
@Slf4j
@Component
public class RegistryServerSync implements NotifyListener {

    /**
     * The constant SUBSCRIBE.
     */
    private static final URL SUBSCRIBE = new URL(Constants.ADMIN_PROTOCOL, NetUtils.getLocalHost(), 0, "",
            Constants.INTERFACE_KEY, Constants.ANY_VALUE,
            Constants.GROUP_KEY, Constants.ANY_VALUE,
            Constants.VERSION_KEY, Constants.ANY_VALUE,
            Constants.CLASSIFIER_KEY, Constants.ANY_VALUE,
            Constants.CATEGORY_KEY, Constants.PROVIDERS_CATEGORY + ","
//            + Constants.CONSUMERS_CATEGORY + ","
//            + Constants.ROUTERS_CATEGORY + ","
            + Constants.CONFIGURATORS_CATEGORY,
            Constants.ENABLED_KEY, Constants.ANY_VALUE,
            Constants.CHECK_KEY, String.valueOf(false));


    /**
     * The Application event publisher.
     */
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Make sure ID never changed when the same url notified many times
     */
    private final ConcurrentHashMap<String, String> URL_IDS_MAPPER = new ConcurrentHashMap<>();

    /**
     * ConcurrentMap<category, ConcurrentMap<servicename, Map<MD5, URL>>>
     * registryCache
     */
    private final ConcurrentMap<String, ConcurrentMap<String, Map<String, URL>>> registryCache = new ConcurrentHashMap<>();

    /**
     * The Registry.
     */
    private  final Registry registry;

    /**
     * Instantiates a new Registry server sync.
     *
     * @param applicationEventPublisher the application event publisher
     * @param registry                  the registry
     */
    public RegistryServerSync(ApplicationEventPublisher applicationEventPublisher, Registry registry) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.registry = registry;
    }

    /**
     * Gets registry cache.
     *
     * @return the registry cache
     */
    public ConcurrentMap<String, ConcurrentMap<String, Map<String, URL>>> getRegistryCache() {
        return registryCache;
    }

    /**
     * Init.
     *
     * @throws Exception the exception
     */
    @PostConstruct
    public void init() throws Exception {
        registry.subscribe(SUBSCRIBE, this);
    }

    /**
     * Destroy.
     *
     * @throws Exception the exception
     */
    @PreDestroy
    public void destroy() throws Exception {
        registry.unsubscribe(SUBSCRIBE, this);
    }

    @Override
    public void notify(List<URL> urls) {
        if (urls == null || urls.isEmpty()) {
            return;
        }
        // category -> serviceName -> MD5 -> URL
        final Map<String, Map<String, Map<String, URL>>> categories = new HashMap<>(200);
        String interfaceName = null;
        for (URL url : urls) {
            log.debug("notify url: {}", url.toFullString());
            String category = url.getParameter(Constants.CATEGORY_KEY, Constants.PROVIDERS_CATEGORY);
            // empty 协议需要删除对应缓存
            if (Constants.EMPTY_PROTOCOL.equalsIgnoreCase(url.getProtocol())) {
                // 发送事件通知
                applicationEventPublisher.publishEvent(new UrlEmptyEvent(this, url));

                ConcurrentMap<String, Map<String, URL>> services = registryCache.get(category);
                if (services != null) {
                    String group = url.getParameter(Constants.GROUP_KEY);
                    String version = url.getParameter(Constants.VERSION_KEY);
                    // NOTE: group and version in empty protocol is *
                    if (!Constants.ANY_VALUE.equals(group) && !Constants.ANY_VALUE.equals(version)) {
                        services.remove(url.getServiceKey());
                    } else {
                        for (Map.Entry<String, Map<String, URL>> serviceEntry : services.entrySet()) {
                            String service = serviceEntry.getKey();
                            if (Tool.getInterface(service).equals(url.getServiceInterface())
                                    && (Constants.ANY_VALUE.equals(group) || StringUtils.isEquals(group, Tool.getGroup(service)))
                                    && (Constants.ANY_VALUE.equals(version) || StringUtils.isEquals(version, Tool.getVersion(service)))) {
                                services.remove(service);
                            }
                        }
                    }
                }
            } else {
                if (StringUtils.isEmpty(interfaceName)) {
                    interfaceName = url.getServiceInterface();
                }
                // 初始化数据
                Map<String, Map<String, URL>> services = categories.computeIfAbsent(category, k -> new HashMap<>(8));
                String service = url.getServiceKey();
                Map<String, URL> ids = services.computeIfAbsent(service, k -> new HashMap<>(8));

                // Make sure we use the same ID for the same URL
                if (URL_IDS_MAPPER.containsKey(url.toFullString())) {
                    ids.put(URL_IDS_MAPPER.get(url.toFullString()), url);
                } else {
                    String md5 = CoderUtil.MD5_16bit(url.toFullString());
                    ids.put(md5, url);
                    URL_IDS_MAPPER.putIfAbsent(url.toFullString(), md5);
                }
            }
        }
        if (categories.size() == 0) {
            return;
        }
        for (Map.Entry<String, Map<String, Map<String, URL>>> categoryEntry : categories.entrySet()) {
            String category = categoryEntry.getKey();
            ConcurrentMap<String, Map<String, URL>> services = registryCache.get(category);
            if (services == null) {
                services = new ConcurrentHashMap<>(8);
                registryCache.put(category, services);
            } else {
                Set<String> keys = new HashSet<>(services.keySet());
                for (String key : keys) {
                    if (Tool.getInterface(key).equals(interfaceName) && !categoryEntry.getValue().entrySet().contains(key)) {
                        services.remove(key);
                    }
                }
            }
            services.putAll(categoryEntry.getValue());
        }
    }
}
