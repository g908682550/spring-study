package com.npu.bean;

import org.springframework.beans.factory.FactoryBean;


//创建一个spring定义的FactoryBean
public class ColorFactory implements FactoryBean<Color> {
    @Override
    public boolean isSingleton() {
        return true;
    }

    //返回一个color对象，这个对象会添加到容器中
    @Override
    public Color getObject() throws Exception {
        return new Color();
    }

    @Override
    public Class<?> getObjectType() {
        return Color.class;
    }
}
