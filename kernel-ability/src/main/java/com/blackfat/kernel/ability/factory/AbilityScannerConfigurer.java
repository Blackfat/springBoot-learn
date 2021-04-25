package com.blackfat.kernel.ability.factory;

import com.blackfat.kernel.ability.core.ExtendAbility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 搜索器通过 ExtendAbilityScan 配置<br/>
 * 生成 AbilityClassPathScanner
 */
@Slf4j
public class AbilityScannerConfigurer implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
    private List<String> basePackages;

    private ApplicationContext applicationContext;

    public void setBasePackages(List<String> basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) {

        log.info("Searching for ability annotated with @ExtendAbility");

        AbilityClassPathScanner scanner = new AbilityClassPathScanner(registry);

        try {
            scanner.setAnnotationClass(ExtendAbility.class);
            scanner.setResourceLoader(this.applicationContext);
            scanner.registerFilters();
            scanner.scan(StringUtils.toStringArray(basePackages));
        } catch (IllegalStateException ex) {
            log.debug("Could not determine auto-configuration package, automatic mapper scanning disabled.", ex);
        }
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) {
        log.debug("do nothing");
    }
}

