package com.npu.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void insertUser(){
        userDao.insert();
        System.out.println("插入完成");
    }

}
