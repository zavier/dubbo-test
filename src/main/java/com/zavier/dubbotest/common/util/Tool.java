package com.zavier.dubbotest.common.util;

/**
 * The type Tool.
 *
 * @date 2020-01-15 12:44
 * @author zhengwei20
 */
public class Tool {

    /**
     * Gets interface.
     *
     * @param service the service
     * @return the interface
     */
    public static String getInterface(String service) {
        if (service != null && service.length() > 0) {
            int i = service.indexOf('/');
            if (i >= 0) {
                service = service.substring(i + 1);
            }
            i = service.lastIndexOf(':');
            if (i >= 0) {
                service = service.substring(0, i);
            }
        }
        return service;
    }

    /**
     * Gets group.
     *
     * @param service the service
     * @return the group
     */
    public static String getGroup(String service) {
        if (service != null && service.length() > 0) {
            int i = service.indexOf('/');
            if (i >= 0) {
                return service.substring(0, i);
            }
        }
        return null;
    }

    /**
     * Gets version.
     *
     * @param service the service
     * @return the version
     */
    public static String getVersion(String service) {
        if (service != null && service.length() > 0) {
            int i = service.lastIndexOf(':');
            if (i >= 0) {
                return service.substring(i + 1);
            }
        }
        return null;
    }
}
