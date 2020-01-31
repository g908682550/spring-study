package com.npu.test;

import com.npu.config.MainConfig2;
import com.npu.config.MainConfigCircle;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IOCTest_circle {

    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigCircle.class);

    @Test
    public void test(){
        applicationContext.getBean("Blue");
    }
}
