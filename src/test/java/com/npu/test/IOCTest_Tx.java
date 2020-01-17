package com.npu.test;

import com.npu.bean.Person;
import com.npu.config.MainConfig2;
import com.npu.tx.TxConfig;
import com.npu.tx.UserService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;
import java.util.Map;

public class IOCTest_Tx {

    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TxConfig.class);

    @Test
    public void test01(){
        UserService userService = applicationContext.getBean(UserService.class);
        userService.insertUser();
    }

}
