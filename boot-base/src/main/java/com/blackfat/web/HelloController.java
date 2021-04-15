package com.blackfat.web;

import com.blackfat.domain.User;
import com.blackfat.encrypt.annotion.Decrypt;
import com.blackfat.encrypt.annotion.Encrypt;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * springboot入门
 *
 * @author blackfat
 * @create 2017-04-06-下午9:18
 */
@RestController
public class HelloController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @Encrypt
    public String index() {
        return "Hello World";
    }


    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @Decrypt
    public String user(@RequestBody User user) {
        System.out.println(user.toString());
        return user.toString();
    }


}
