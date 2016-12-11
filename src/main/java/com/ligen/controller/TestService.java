package com.ligen.controller;

import org.springframework.stereotype.Service;

/**
 * Created by ligen on 2016/12/11.
 */
@Service
public class TestService {

    public TestService() {
        System.out.println("TestService");
    }
    public String hello() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello";
    }
}
