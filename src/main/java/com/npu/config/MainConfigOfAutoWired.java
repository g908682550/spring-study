package com.npu.config;

import com.npu.dao.BookDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 自动装配；
 *      Spring利用依赖注入（DI），完成对IOC容器中各个组件的依赖关系赋值；
 *      1、@AutoWired：自动注入【Spring定义的】
 *          1、默认按照类型去容器中找对应的组件    applicationContext.getBean(BookService.class)，找到就赋值
 *          2、如果找到相同类型的组件，再将属性的名称作为组件的id去容器中查找      applicationContext.getBean("bookDao")
 *          3、@Qualifier("bookDao")：使用该注解来指定需要装配的组件的id，而不是使用属性名
 *          4、自动装配默认一定要将属性赋值好，没有就会报错，可通过在Autowire的注解中将required=false来使该配置设置为非必需
 *          5、@Primary：让Spring进行自动装配的时候，默认使用首选的bean,也可以继续使用@Qualifier来指定需要装配的bean
 *          BookService{
 *              @Autowired
 *              BookDao bookDao;
 *          }
 *      2、Spring还支持使用@Resource（JSR250）和@Inject（JSR330）【java规范】
 *          1、@Resource：
 *              可以和@Autowired一样实现自动装配功能；默认是按照组件名称进行装配的；没有能支持@Primary的功能以及@Autowired（required=false）的功能
 *          2、@Inject（需要导入依赖）：
 *              导入javax.inject的包，和Autowired的功能一样，没有required=false的功能
 *
 *      AutowiredAnnotationBeanPostProcessor：解析完成自动装配功能
 *
 *      3、@Autowired：构造器，参数，方法，属性
 *          1）标注在方法位置     标注在方法，Spring容器创建当前对象，就会调用方法，完成赋值，方法使用的参数，自定义类型的值从ioc容器中获取
 *                               @Bean标注的方法创建对象的时候，方法参数的值默认从ioc容器中获取，默认不写Autowired，效果是一样的
 *          2）标注在构造器位置   默认加在ioc容器中的组件，容器启动会调用无参构造器创建对象，再进行初始化赋值等操作。
 *                               标注在构造器上可以默认调用该方法，方法中用的参数同样从ioc容器中获取，如果容器只有一个有参构造器，这个有参构造器的Autowired可以省略，参数位置的组件还是可以自动从容器中获取
 *          3）标注在参数位置     从ioc容器中获取参数组件的值
 *      4、自定义组件想要使用Spring容器底层的一些组件（ApplicationContext，BeanFactory，xxx）;
 *           自定义组件需要实现xxxAware接口；在创建对象的时候会调用接口规定的方法注入相关组件；
 *           把Spring底层的一些组件注入到自定义的bean中
 *           xxxAware的功能都是使用xxxAwareProcessor处理的
 *
 */
@Configuration
@ComponentScan({"com.npu.service","com.npu.dao","com.npu.controller","com.npu.bean"})
public class MainConfigOfAutoWired {

    @Primary
    @Bean("bookDao2")
    public BookDao bookDao(){
        return new BookDao();
    }

}
