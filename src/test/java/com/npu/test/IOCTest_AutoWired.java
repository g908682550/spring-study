package com.npu.test;

import com.npu.bean.Boss;
import com.npu.bean.Car;
import com.npu.config.MainConfigOfAutoWired;
import com.npu.service.BookService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IOCTest_AutoWired {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAutoWired.class);
    @Test
    public void test01(){
//        printBeans(applicationContext);
        Boss bean = applicationContext.getBean(Boss.class);
        Car beanCar = applicationContext.getBean(Car.class);
        System.out.println(bean);
        System.out.println(beanCar);
    }

    private void printBeans(AnnotationConfigApplicationContext applicationContext){
        String[] definitionNames=applicationContext.getBeanDefinitionNames();
        for(String name:definitionNames){
            System.out.println(name);
        }
    }
}
