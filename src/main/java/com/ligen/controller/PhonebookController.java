package com.ligen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ligen on 2016/12/11.
 */
@Controller
public class PhonebookController {


    public PhonebookController() {
        System.out.println("PhonebookController");
    }

    @RequestMapping(value="/post",method= RequestMethod.POST)
    @ResponseBody
    public String handleFoo2(@RequestBody String body) {

        return body;
    }
}
