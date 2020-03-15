package com.zavier.dubbotest.model;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * The type Invoke method param.
 *
 * @date 2020-01-15 12:41
 * @author zhengwei20
 */
@Data
public class InvokeMethodParam {
    /**
     * The Id.
     */
    private String id;
    /**
     * The Service key.
     */
    private String serviceKey;
    /**
     * The Method.
     */
    private String method;
    /**
     * The Param infos.
     */
    private List<ParamInfo> paramInfos;

    /**
     * The Json array params.
     */
    private List<Object> jsonArrayParams;
    /**
     * The App.
     */
    private String app;
    /**
     * The App version.
     */
    private String appVersion;

    /**
     * Has app info boolean.
     *
     * @return the boolean
     */
    public boolean hasAppInfo() {
        return StringUtils.isNotBlank(app) && StringUtils.isNotBlank(appVersion);
    }

    /**
     * Has param type boolean.
     *
     * @return the boolean
     */
    public boolean hasParamType() {
        return jsonArrayParams == null || jsonArrayParams.size() == 0;
    }
}
