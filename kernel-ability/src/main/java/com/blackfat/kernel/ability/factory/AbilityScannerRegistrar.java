package com.blackfat.kernel.ability.factory;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 能力自动初始化搜索器，搜索 声明为扩展点的 接口
 */
public class AbilityScannerRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     * 设置 scanner 参数
     *
     * @param importingClassMetadata 声明信息
     * @param registry 注册器
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
                                        @NonNull BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(ExtendAbilityScan.class.getName()));

        if (attributes != null) {
            registerBeanDefinitions(attributes, registry, generateBaseBeanName(importingClassMetadata));
        }
    }

    private static String generateBaseBeanName(AnnotationMetadata importingClassMetadata) {
        return importingClassMetadata.getClassName() + "#" + AbilityScannerRegistrar.class.getSimpleName() + "#0";
    }

    private void registerBeanDefinitions(AnnotationAttributes annotationAttributes, BeanDefinitionRegistry registry,
                                         String beanName) {

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(AbilityScannerConfigurer.class);

        List<String> basePackages = Arrays.stream(annotationAttributes.getStringArray("basePackages"))
                .filter(StringUtils::hasText).collect(Collectors.toList());
        builder.addPropertyValue("basePackages", basePackages);

        registry.registerBeanDefinition(beanName, builder.getBeanDefinition());
    }
}

