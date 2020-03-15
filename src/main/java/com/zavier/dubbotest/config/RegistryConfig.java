package com.zavier.dubbotest.config;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.RegistryFactory;
import com.zavier.dubbotest.common.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Registry config.
 *
 * @date 2020-01-15 12:43
 * @author zhengwei20
 */
@Configuration
public class RegistryConfig {

    /**
     * The Registry address.
     */
    @Value("${registry.address}")
    private String registryAddress;

    /**
     * The Registry group.
     */
    @Value("${registry.group}")
    private String registryGroup;

    /**
     * The Registry url.
     */
    private URL registryUrl;

    /**
     * Gets registry.
     *
     * @return the registry
     */
    @Bean
    Registry getRegistry() {
        if (StringUtils.isBlank(registryAddress)) {
            throw new BusinessException("registryAddress can not be empty");
        }

        registryUrl = URL.valueOf(registryAddress);
        if (StringUtils.isNotEmpty(registryGroup)) {
            registryUrl = registryUrl.addParameter(Constants.GROUP_KEY, registryGroup);
        }
        RegistryFactory registryFactory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getAdaptiveExtension();
        return registryFactory.getRegistry(registryUrl);
    }
}
