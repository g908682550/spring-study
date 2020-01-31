package com.npu.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Boss {

    @Autowired
    Blue blue;

    Car car;

    @Override
    public String toString() {
        return "Boss{" +
                "car=" + car +
                '}';
    }

    public Car getCar() {
        return car;
    }

    //@Autowired
    //标注在方法，Spring容器创建当前对象，就会调用方法，完成赋值
    //方法使用的参数，自定义类型的值从ioc容器中获取
    public void setCar(Car car) {
        this.car = car;
    }
}
