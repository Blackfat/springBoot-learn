package com.blackfat.boot2.annoation;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.stream.Stream;

/**
 * @author wangfeiyang
 * @Description {@link Server} {@link ImportBeanDefinitionRegistrar}
 * @create 2019-06-26 17:35
 * @since 1.0-SNAPSHOT
 */
public class ServerImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        ImportSelector importSelector = new ServerImportSelector();
        // 筛选 Class 名称集合
        String[] selectedClassNames = importSelector.selectImports(annotationMetadata);
        Stream.of(selectedClassNames)
                .map(BeanDefinitionBuilder::genericBeanDefinition)
                .map(BeanDefinitionBuilder::getBeanDefinition)
                .forEach(beanDefinition -> {
                    // 注册 BeanDefinition 到 BeanDefinitionRegistry
                    BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, beanDefinitionRegistry);
                });
    }
}
