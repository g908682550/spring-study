package com.npu.bean;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class Dog {
    public Dog() {
        System.out.println("dog constructor");
    }

    //容器创建并赋值之后调用
    @PostConstruct
    public void init(){
        System.out.println("dog init...");
    }

    //容器销毁之前
    @PreDestroy
    public void destroy(){
        System.out.println("dog destroy...");
    }
}
