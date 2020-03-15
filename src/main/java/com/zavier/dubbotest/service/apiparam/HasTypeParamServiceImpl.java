package com.zavier.dubbotest.service.apiparam;

import com.alibaba.fastjson.JSON;
import com.zavier.dubbotest.model.InvokeMethodParam;
import com.zavier.dubbotest.model.ParamInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户已经指向参数类型
 *
 * @date 2020-01-15 12:41
 * @author zhengwei20
 */
@Slf4j
@Service
public class HasTypeParamServiceImpl implements ParamService {

    @Override
    public ApiParamInfo getApiParamInfo(InvokeMethodParam invokeMethodParam) {
        List<String> paramTypes = new ArrayList<>();
        List<Object> paramObjs = new ArrayList<>();
        List<ParamInfo> paramInfos = invokeMethodParam.getParamInfos();
        for (ParamInfo paramInfo : paramInfos) {
            String type = paramInfo.getType();
            paramTypes.add(type);
            if (!type.startsWith("java.")) {
                Map map = JSON.parseObject(paramInfo.getParam(), Map.class);
                paramObjs.add(map);
            } else {
                String param = paramInfo.getParam();
                paramObjs.add(param);
            }
        }
        return new ApiParamInfo(paramTypes, paramObjs);
    }
}
