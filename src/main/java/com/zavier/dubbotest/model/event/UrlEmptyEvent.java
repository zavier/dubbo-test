package com.zavier.dubbotest.model.event;

import com.alibaba.dubbo.common.URL;
import org.springframework.context.ApplicationEvent;

/**
 * The type Url empty event.
 *
 * @date 2020-01-15 12:44
 * @author zhengwei20
 */
public class UrlEmptyEvent extends ApplicationEvent {

    /**
     * The Url.
     */
    private final URL url;

    /**
     * Instantiates a new Url empty event.
     *
     * @param source the source
     * @param url    the url
     */
    public UrlEmptyEvent(Object source, URL url) {
        super(source);
        this.url = url;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public URL getUrl() {
        return url;
    }
}
