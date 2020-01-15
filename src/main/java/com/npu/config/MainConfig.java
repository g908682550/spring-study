package com.npu.config;

import com.npu.bean.Person;
import com.npu.service.BookService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

//配置类==配置文件
@Configuration//告诉Spring这是一个配置类
//@ComponentScan(value = "com.npu",excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class})})
//@ComponentScan value:指定要扫描的包
//excludeFilters=Filter[]:指定扫描包的时候按照什么规则排除哪些组件
//includeFilters=Filter[]:指定扫描包的时候要包含哪些组件,需将useDefaultFilters置为false
//FilterType.ANNOTATION:按照注解
//FilterType.ASSIGNABLE_TYPE:按照指定的类型
//FilterType.REGEX:使用正则指定
//FilterType.CUSTOM:使用自定义规则
//@ComponentScan(value = "com.npu",excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = {BookService.class})})
@ComponentScan(value = "com.npu",includeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM,classes = {MyTypeFilter.class})},useDefaultFilters = false)
public class MainConfig {

    //给容器注册一个bean，类型为返回值的类型，id默认用方法名作为类型
    @Bean("person")
    public Person Person(){
        return new Person("gy",20);
    }

}
