package com.ligen.controller;

import com.ligen.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class TestController {
    //@Autowired
//    RedisTemplate<String, Serializable> redisTemplate ;

    @Autowired
    TestService service;

    public TestController() {
        System.out.println("TestController");
    }

    @RequestMapping("/foo")
    @ResponseBody
    public String handleFoo() {
        return service.hello();
    }

    @RequestMapping(value="/foo2",method= RequestMethod.POST)
    @ResponseBody
    public String handleFoo2(@RequestBody String body) {

        return body;
    }


    @RequestMapping(value="/foo3",method= RequestMethod.GET)
    @ResponseBody
    public String handleFoo3() {

//        ValueOperations<String, Serializable> ops = null;
//        ops = redisTemplate.opsForValue();
//        ops.set("1","2");

//        return  ops.get("1").toString();
        return "hahaha";
    }
}