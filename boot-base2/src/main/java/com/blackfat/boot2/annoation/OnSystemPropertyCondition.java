package com.blackfat.boot2.annoation;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-04-26 19:41
 * @since 1.0-SNAPSHOT
 */
public class OnSystemPropertyCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MultiValueMap<String,Object>  attributes =
                metadata.getAllAnnotationAttributes(ConditionalOnSystemProperty.class.getName());
        String propertyName = (String)attributes.getFirst("name");
        String propertyValue = (String) attributes.getFirst("value");
        // 获取 系统属性值
        String systemPropertyValue = System.getProperty(propertyName);
        // 比较 系统属性值 与 ConditionalOnSystemProperty#value() 方法值 是否相等
        if (Objects.equals(systemPropertyValue, propertyValue)) {
            System.out.printf("系统属性[名称 : %s] 找到匹配值 : %s\n",propertyName,propertyValue);
            return true;
        }
        return false;
    }
}
