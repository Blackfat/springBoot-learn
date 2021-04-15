package com.blackfat.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author wangfeiyang
 * @desc
 * @create 2017/4/7-14:06
 */
@Controller
public class ThymeleafController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("host", "https://www.baidu.com");
        return "index";
    }


}
