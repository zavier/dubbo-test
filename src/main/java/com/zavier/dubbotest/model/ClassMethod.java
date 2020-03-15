package com.zavier.dubbotest.model;

import lombok.Data;

import java.util.List;

/**
 * 服务的方法参数信息
 *
 * @date 2020-01-15 12:40
 * @author zhengwei20
 */
@Data
public class ClassMethod {

    /**
     * The Class name.
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数信息
     */
    private List<ParamPair> paramInfoList;

}
