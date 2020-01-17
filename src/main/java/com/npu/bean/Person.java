package com.npu.bean;

import org.springframework.beans.factory.annotation.Value;

public class Person {

    //@Value赋值
    //1、基本数值
    //2、可以写SpEL;#{}
    //3、可以写${}取出配置文件的值（在运行环境变量里面的值）
    @Value("gy")
    private String name;
    @Value("18")
    private Integer age;

    @Value("${person.nickName}")
    private String nickName;
    public Person() {
    }

    public Person(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", nickName='" + nickName + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
