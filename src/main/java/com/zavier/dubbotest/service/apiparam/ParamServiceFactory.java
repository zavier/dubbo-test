package com.zavier.dubbotest.service.apiparam;

import com.zavier.dubbotest.model.InvokeMethodParam;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * The type Param service factory.
 *
 * @date 2020-01-15 12:43
 * @author zhengwei20
 */
@Component
public class ParamServiceFactory implements ApplicationContextAware {

    /**
     * The Application context.
     */
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Gets param service.
     *
     * @param invokeMethodParam the invoke method param
     * @return the param service
     */
    public ParamService getParamService(InvokeMethodParam invokeMethodParam) {
        boolean hasParamType = invokeMethodParam.hasParamType();
        if (hasParamType) {
            return applicationContext.getBean(HasTypeParamServiceImpl.class);
        }
        return applicationContext.getBean(NoTypeParamServiceImpl.class);
    }
}
