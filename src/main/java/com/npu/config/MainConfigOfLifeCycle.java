package com.npu.config;

import com.npu.bean.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * bean的生命周期
 *      bean创建--初始化--销毁的过程
 * 可以自定义初始化和销毁方法，容器在bean进行到当前声明周期的时候来调用我们自定义的初始化和销毁方法
 * 构造（对象创建）
 *       单实例：在容器启动时创建
 *       多实例：每次获取时创建
 * 初始化：对象创建时
 * 销毁：
 *      单实例：关闭容器时
 *      多实例：容器不会管理这个bean，容器不会调用销毁方法、
 *
 * 遍历得到容器中所有的BeanPostProcessor；挨个执行beforeInitialization
 * 一旦返回null，跳出for循环，不会执行后面的BeanPostProcess.postProcessors
 * BeanPostProcessor的大致执行流程
 *populateBean(beanName, mbd, instanceWrapper);给bean进行属性赋值
 *initializeBean{
 * applyBeanPostProcessorsBeforeInitialization//for循环得到全部beanPost
 *  invokeInitMethods(beanName, wrappedBean, mbd);//初始化方法
 * applyBeanPostProcessorsAfterInitialization//for循环得到全部beanPost
 *}
 *
 *
 * 1）指定初始化和销毁方法 在@Bean注解里指定init方法和destroy方法
 * 2）通过让bean实现InitializingBean（定义初始化逻辑），DisposableBean（定义销毁逻辑）接口
 * 3）可以使用JSR250
 *      1、@PostConstructor：在bean创建完成并且属性赋值完成，来执行初始化方法
 *      2、@PreDestroy：在容器销毁bean之前通知我们进行清理工作
 * 4）BeanPostProcessor【接口】：bean的后置处理器
 *      在bean初始化前后进行一些处理工作
 *          postProcessBeforeInitialization：在初始化之前进行一些工作
 *          对象初始化
 *          postProcessAfterInitialization：在初始化之后进行一些工作
 * Spring底层对BeanPostProcess接口的使用
 *      bean赋值，注入其它组件，@AutoWired，生命周期注解功能 @Async xxx都是通过BeanPostProcess进行完成的
 *      ApplicationContextAwareProcessor会在实现了ApplicationContextAware接口的bean里面
 *      通过((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);将容器注入到bean中
 *
 *      InitDestroyAnnotationBeanPostProcessor会对@PostConstructor和@PreDestroy注解进行解析来达到容器的初始化和销毁方法的执行
 */
@Configuration
@ComponentScan("com.npu.bean")
public class MainConfigOfLifeCycle {

//    @Bean(initMethod = "init",destroyMethod = "destroy")
//    public Car car(){
//        return new Car();
//    }

}
