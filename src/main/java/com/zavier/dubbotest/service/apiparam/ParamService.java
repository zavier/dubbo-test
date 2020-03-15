package com.zavier.dubbotest.service.apiparam;

import com.zavier.dubbotest.model.InvokeMethodParam;

/**
 * The interface Param service.
 *
 * @date 2020-01-15 12:43
 * @author zhengwei20
 */
public interface ParamService {

    /**
     * 获取dubbo调用所需的参数类型和参数值集合
     *
     * @param invokeMethodParam the invoke method param
     * @return the api param info
     */
    ApiParamInfo getApiParamInfo(InvokeMethodParam invokeMethodParam);
}
