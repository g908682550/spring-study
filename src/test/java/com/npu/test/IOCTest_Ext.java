package com.npu.test;

import com.npu.config.MainConfig2;
import com.npu.ext.ExtConfig;
import org.junit.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IOCTest_Ext {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExtConfig.class);
    @Test
    public void fun(){
        applicationContext.publishEvent(new ApplicationEvent(new String("我发布的事件")) {
        });
        applicationContext.close();
    }
}
