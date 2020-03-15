package com.zavier.dubbotest.service.apiparam;

import lombok.Data;

import java.util.List;

/**
 * 接口参数信息
 *
 * @date 2020-01-15 12:39
 * @author zhengwei20
 */
@Data
public class ApiParamInfo {
    /**
     * 参数类型
     */
    private List<String> paramTypeList;

    /**
     * 参数值
     */
    private List<Object> paramDataList;

    /**
     * Instantiates a new Api param info.
     */
    public ApiParamInfo() {
    }

    /**
     * Instantiates a new Api param info.
     *
     * @param paramTypeList the param type list
     * @param paramDataList the param data list
     */
    public ApiParamInfo(List<String> paramTypeList, List<Object> paramDataList) {
        this.paramTypeList = paramTypeList;
        this.paramDataList = paramDataList;
    }
}
