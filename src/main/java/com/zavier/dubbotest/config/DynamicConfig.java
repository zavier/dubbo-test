package com.zavier.dubbotest.config;

import com.alibaba.dubbo.common.URL;

/**
 * The interface Dynamic config.
 *
 * @date 2020-01-15 12:41
 * @author zhengwei20
 */
public interface DynamicConfig {
    /**
     * Sets url.
     *
     * @param url the url
     */
    void setUrl(URL url);

    /**
     * Gets url.
     *
     * @return the url
     */
    URL getUrl();

    /**
     * Init.
     */
    void init();
}
