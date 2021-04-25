package com.blackfat.kernel.ability.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

/**
 * 对于 ExtendAbilityScan 设置的包进行 遍历检查，每个ability生成代理
 */
@Slf4j
public class AbilityClassPathScanner extends ClassPathBeanDefinitionScanner {

    private Class<? extends Annotation> annotationClass;

    public AbilityClassPathScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    /**
     * 注册 监听的 注释
     */
    public void registerFilters() {

        // 监听 ability 的 annotation
        if (this.annotationClass != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        }

        // exclude package-info.java
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    @Override
    @NonNull
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {

        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            log.warn("No ability  was found in {} package. Please check your configuration.",
                    Arrays.toString(basePackages));
        } else {
            processBeanDefinitions(beanDefinitions);
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition)holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            log.debug("Creating ability with name {} and {} interface", holder.getBeanName(), beanClassName);

            if (StringUtils.isEmpty(beanClassName)) {
                log.error("class name not found {}", definition);
                continue;
            }
            definition.setBeanClass(AbilityFactoryBean.class);
            definition.getConstructorArgumentValues().addIndexedArgumentValue(0, beanClassName);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) {
        if (super.checkCandidate(beanName, beanDefinition)) {
            return true;
        } else {
            log.warn("Skipping AbilityFactoryBean with name {} and {} abilityInterface"
                    + ". Bean already defined with the same name!", beanName, beanDefinition.getBeanClassName());
            return false;
        }
    }
}
