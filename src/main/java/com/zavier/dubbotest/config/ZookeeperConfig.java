package com.zavier.dubbotest.config;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * The type Zookeeper config.
 *
 * @date 2020-01-15 12:44
 * @author zhengwei20
 */
public class ZookeeperConfig implements DynamicConfig {

    /**
     * The Zk client.
     */
    private CuratorFramework zkClient;
    /**
     * The Url.
     */
    private URL url;
    /**
     * The Root.
     */
    private String root;

    /**
     * The constant DEFAULT_ROOT.
     */
    private static final String DEFAULT_ROOT = "dubbo";

    @Override
    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public void init() {
        if (url == null) {
            throw new IllegalStateException("server url is null, cannot init");
        }
        CuratorFrameworkFactory.Builder zkClientBuilder = CuratorFrameworkFactory.builder().
                connectString(url.getAddress()).
                retryPolicy(new ExponentialBackoffRetry(1000, 3));
        zkClient = zkClientBuilder.build();
        String group = url.getParameter(Constants.GROUP_KEY, DEFAULT_ROOT);
        if (!group.startsWith(Constants.PATH_SEPARATOR)) {
            group = Constants.PATH_SEPARATOR + group;
        }
        root = group;
        zkClient.start();
    }
}
