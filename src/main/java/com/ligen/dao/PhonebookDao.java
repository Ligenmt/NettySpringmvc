package com.ligen.dao;

import org.springframework.stereotype.Repository;

/**
 * Created by ligen on 2016/12/11.
 */
@Repository
public class PhonebookDao extends AbstractMongoDao {

    public PhonebookDao() {
    	super("phonebook");
        System.out.println("PhonebookDao");
    }
}
