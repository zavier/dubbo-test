package com.zavier.dubbotest.model;

import lombok.Data;

import java.util.List;

/**
 * The type Provider dto.
 *
 * @date 2020-01-15 12:43
 * @author zhengwei20
 */
@Data
public class ProviderDTO {

    /**
     * The Id.
     */
    private String id;

    /**
     * The Service key.
     */
    private String serviceKey;

    /**
     * The Service interface.
     */
    private String serviceInterface;

    /**
     * The Version.
     */
    private String version;

    /**
     * The Ip.
     */
    private String ip;

    /**
     * The Port.
     */
    private Integer port;

    /**
     * The Methods.
     */
    private List<String> methods;

}
