package com.npu.condition;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class WindowsCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //能获取到ioc使用的beanFactory工厂
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        //获取类加载器
        ClassLoader classLoader = context.getClassLoader();

        //获取当前环境信息
        Environment environment = context.getEnvironment();

        //获取bean定义的注册类
        BeanDefinitionRegistry registry = context.getRegistry();

        //可以判断容器中bean注册情况，也可以给容器中注册bean
        registry.getBeanDefinitionNames();

        String property = environment.getProperty("os.name");
        if(property.contains("Windows")) return true;
        return false;
    }
}
