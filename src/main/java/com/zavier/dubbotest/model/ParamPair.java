package com.zavier.dubbotest.model;


import lombok.Data;

/**
 * The type Param pair.
 *
 * @date 2020-01-15 12:43
 * @author zhengwei20
 */
@Data
public class ParamPair {
    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * Instantiates a new Param pair.
     *
     * @param paramName the param name
     * @param paramType the param type
     */
    public ParamPair(String paramName, String paramType) {
        this.paramName = paramName;
        this.paramType = paramType;
    }
}