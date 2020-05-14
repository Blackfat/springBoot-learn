package com.blackfat.debug.apidesign.headerapiversion;

import com.blackfat.debug.apidesign.apiversion.APIVersionHandlerMapping;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author wangfeiyang
 * @Description
 * @create 2020-05-08 16:34
 * @since 1.0-SNAPSHOT
 */
@SpringBootApplication
public class CommonMistakesApplication implements WebMvcRegistrations {

    public static void main(String[] args) {
        SpringApplication.run(CommonMistakesApplication.class, args);
    }

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new APIVersionHandlerMapping();
    }
}
