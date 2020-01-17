package com.npu.test;

import com.npu.aop.MathCalculator;
import com.npu.bean.Person;
import com.npu.config.MainConfig2;
import com.npu.config.MainConfigOfAOP;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;
import java.util.Map;

public class IOCTest_AOP {

    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAOP.class);

    @Test
    public void test01(){
        MathCalculator bean = applicationContext.getBean(MathCalculator.class);
        bean.div(3,2);
    }


}
