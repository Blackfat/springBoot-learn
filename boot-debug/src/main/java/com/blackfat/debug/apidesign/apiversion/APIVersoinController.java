package com.blackfat.debug.apidesign.apiversion;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangfeiyang
 * @Description
 * @create 2020-05-08 16:28
 * @since 1.0-SNAPSHOT
 */
@Slf4j
@RequestMapping("apiversion")
@RestController
@APIVersion("v1")
public class APIVersoinController {

    @GetMapping(value = "/api/user")
    @APIVersion("v4")
    public int right4() {
        return 4;
    }
}
