package com.blackfat.debug.annotation;

import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-04-22 10:27
 * @since 1.0-SNAPSHOT
 */
@TransactionalService(name = "test")
public class TransactionalServiceAnnotationReflectionBootstrap {

    public static void main(String[] args) {
        AnnotatedElement annotatedElement = TransactionalServiceAnnotationReflectionBootstrap.class;
        TransactionalService transactionalService = annotatedElement.getAnnotation(TransactionalService.class);
//        printAnnotationAttribute(transactionalService);
        // 获取 transactionalService 的所有的元注解
        Set<Annotation> metaAnnotations = getAllMetaAnnotations(transactionalService);
        // 输出结果
        metaAnnotations.forEach(TransactionalServiceAnnotationReflectionBootstrap::printAnnotationAttribute);
    }

    private static Set<Annotation> getAllMetaAnnotations(Annotation annotation) {

        Annotation[] metaAnnotations = annotation.annotationType().getAnnotations();

        if (ObjectUtils.isEmpty(metaAnnotations)) { // 没有找到，返回空集合
            return Collections.emptySet();
        }
        // 获取所有非 Java 标准元注解结合
        Set<Annotation> metaAnnotationsSet = Stream.of(metaAnnotations)
                // 排除 Java 标准注解，如 @Target，@Documented 等，它们因相互依赖，将导致递归不断
                // 通过 java.lang.annotation 包名排除
                .filter(metaAnnotation -> !Target.class.getPackage().equals(metaAnnotation.annotationType().getPackage()))
                .collect(Collectors.toSet());

        // 递归查找元注解的元注解集合
        Set<Annotation> metaMetaAnnotationsSet = metaAnnotationsSet.stream()
                .map(TransactionalServiceAnnotationReflectionBootstrap::getAllMetaAnnotations)
                .collect(HashSet::new, Set::addAll, Set::addAll);

        // 添加递归结果
        metaAnnotationsSet.addAll(metaMetaAnnotationsSet);

        return metaAnnotationsSet;
    }

    private static void printAnnotationAttribute(Annotation annotation) {
        Class<?> annotationType = annotation.annotationType();
        // 完全 Java 反射实现（ReflectionUtils 为 Spring 反射工具类）
//        ReflectionUtils.doWithMethods(annotationType,
//                method -> System.out.printf("@%s.%s() = %s\n", annotationType.getSimpleName(),
//                        method.getName(), ReflectionUtils.invokeMethod(method, annotation)) // 执行 Method 反射调用
//               , method -> method.getParameterCount() == 0); // 选择无参数方法
//                , method -> !method.getDeclaringClass().equals(Annotation.class));// 选择非 Annotation 方法
        ReflectionUtils.doWithMethods(annotationType,
                method -> System.out.printf("@%s.%s() = %s\n", annotationType.getSimpleName(), method.getName(), ReflectionUtils.invokeMethod(method, annotation)),
                method -> !method.getDeclaringClass().equals(Annotation.class)
                );
    }
}
