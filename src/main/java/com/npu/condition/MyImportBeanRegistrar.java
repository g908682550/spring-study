package com.npu.condition;

import com.npu.bean.Blue;
import com.npu.bean.Yello;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class MyImportBeanRegistrar implements ImportBeanDefinitionRegistrar {

    /**
     *
     * @param importingClassMetadata 当前类的注解信息
     * @param registry               BeanDefinition注册类，可以通过该类来注册bean
     * @param importBeanNameGenerator
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        boolean definition1 = registry.containsBeanDefinition("com.npu.bean.Blue");
        boolean definition2 = registry.containsBeanDefinition("com.npu.bean.Color");
        if(definition1&&definition2){
            RootBeanDefinition beanDefinition = new RootBeanDefinition(Yello.class);
            registry.registerBeanDefinition("yello",beanDefinition);
        }
    }
}
