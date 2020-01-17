package com.npu.config;

import org.springframework.context.annotation.Configuration;

/**
 * Profile:
 *      Spring为我们提供的可以根据当前环境，动态的激活和切换一系列组件的功能；
 *
 * 开发环境、测试环境、生产环境
 * 数据源（/A)(/B)(/C)
 *
 * @Profile:指定组件在哪个环境的情况下才能被注册到容器中，不指定，任何环境下都能注册
 *
 * 1、加了环境标识的bean，只有这个环境被激活的时候才能注册到容器中，默认是default环境
 * 2、写在配置类上，只有是指定的环境的时候，整个配置类里面的所有配置才能生效
 * 3、没有标注环境标识的bean在任何环境下都加载
 *
 * 运行时如何指定运行环境：
 *  1、命令行参数，通过在虚拟机参数位置指定-Dspring.profiles.active=xxx来指定运行环境，标注了该环境的bean会被配置进容器中
 *  2、程序内指定：
 *          1、创建一个applicationContext
 *          2、设置需要激活的环境，applicationContext.getEnvironment().setActiveProfiles("");
 *          3、注册主配置类，applicationContext.register(xxx.class)
 *          4、启动刷新容器，applicationContext.refresh();
 */
@Configuration
public class MainConfigOfProfile {
}
