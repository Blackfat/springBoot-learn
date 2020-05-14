package com.blackfat.debug.apidesign.headerapiversion;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * @author wangfeiyang
 * @Description
 * @create 2020-05-08 16:23
 * @since 1.0-SNAPSHOT
 */
public class APIVersionHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return AnnotatedElementUtils.hasAnnotation(beanType, Controller.class);
    }


    @Override
    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
        APIVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, APIVersion.class);
        return createCondition(apiVersion);
    }

    @Override
    protected RequestCondition<?> getCustomMethodCondition(Method method) {
        APIVersion apiVersion = AnnotationUtils.findAnnotation(method, APIVersion.class);
        return createCondition(apiVersion);
    }


    private RequestCondition<APIVersionCondition> createCondition(APIVersion apiVersion) {
        return apiVersion == null ? null : new APIVersionCondition(apiVersion.value(), apiVersion.headerKey());
    }

}
