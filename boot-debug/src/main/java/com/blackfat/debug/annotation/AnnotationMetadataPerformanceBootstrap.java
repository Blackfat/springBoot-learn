package com.blackfat.debug.annotation;

import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;

import java.io.IOException;

/**
 * @author wangfeiyang
 * @Description
 * @create 2021-04-22 16:20
 * @since 1.0-SNAPSHOT
 */
public class AnnotationMetadataPerformanceBootstrap {

    public static void main(String[] args) throws IOException {
        // 反射实现
        AnnotationMetadata standardAnnotationMetadata = new StandardAnnotationMetadata(TransactionalService.class);

        SimpleMetadataReaderFactory factory = new SimpleMetadataReaderFactory();

        MetadataReader metadataReader = factory.getMetadataReader(TransactionalService.class.getName());
        // ASM 实现
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();

        standardAnnotationMetadata.getAnnotationTypes();

        annotationMetadata.getAnnotationTypes();
    }
}
