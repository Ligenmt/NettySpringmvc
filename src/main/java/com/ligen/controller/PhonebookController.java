package com.ligen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ligen.dao.PhonebookDao;

/**
 * Created by ligen on 2016/12/11.
 */
@Controller
public class PhonebookController {

	
	@Autowired
	PhonebookDao dao;

    public PhonebookController() {
        System.out.println("PhonebookController");
    }

    @RequestMapping(value="/post",method= RequestMethod.POST)
    @ResponseBody
    public String handleFoo2(@RequestBody String body) {    	
    	
    	
        return body;
    }
    
    @RequestMapping(value="/getInfo",method= RequestMethod.POST, produces="text/plain;charset=utf-8")
    @ResponseBody
    public String getInfo(@RequestBody String body) throws Exception {    	
    	
    	JSONObject params = JSON.parseObject(body);
    	
    	String phone = params.getString("phone");
    	
    	Query query = new Query();
    	query.addCriteria(Criteria.where("_id").is(phone));
		JSONObject found = dao.getMongoTemplate().findOne(query , JSONObject.class, "phonebook");
    	
    	
        return found.toJSONString();
    }
    
    
}
