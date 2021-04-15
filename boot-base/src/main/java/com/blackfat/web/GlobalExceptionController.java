
package com.blackfat.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author wangfeiyang
 * @desc
 * @create 2017/4/7-14:59
 */
@Controller
public class GlobalExceptionController {

    @RequestMapping(value = "/exception", method = RequestMethod.GET)
    public String exceptionHandler() throws Exception {
        throw new Exception("发生异常！");
    }
}
