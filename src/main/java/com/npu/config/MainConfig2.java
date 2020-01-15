package com.npu.config;

import com.npu.bean.Color;
import com.npu.bean.ColorFactory;
import com.npu.bean.Person;
import com.npu.condition.LinuxCondition;
import com.npu.condition.MyImportBeanRegistrar;
import com.npu.condition.MyImportSelector;
import com.npu.condition.WindowsCondition;
import org.springframework.context.annotation.*;

/**
 * 给容器中中注册组件
 * 1、包扫描+组件标注注解（@Component、@Service、@Controller、@Repository，主要是自己写的类
 * 2、@Bean[导入的第三方包里面的组件]
 * 3、@Import[快速给容器中导入一个组件]
 *          1、Import(类名),容器中就会自动注册这个组件，id默认是组件的全名
 *          2、ImportSelector：返回需要导入的组件的全类名的数组
 *          3、ImportBeanDefinitionRegistrar：手动注册bean
 * 4、使用Spring提供的FactoryBean（工厂bean）
 *          1、默认获取到的是工厂bean调用getObject创建的对象
 *          2、要获取到bean本身，需要给id前面加个&标识
 */
@Configuration
@Import({Color.class, MyImportSelector.class, MyImportBeanRegistrar.class,MyImportBeanRegistrar.class})
public class MainConfig2 {

    //默认是单例的

    /**
     * 	 * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE prototype
     * 	 * @see ConfigurableBeanFactory#SCOPE_SINGLETON singleton
     * 	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST request
     * 	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION session
     * @return
     *
     * prototype:多例的 ioc容器启动并不会去调用方法创建对象在容器中，而是每次获取时才会调用方法创建对象
     * singleton:单例的（默认值） ioc容器启动会调用方法创建对象放到ioc容器中，以后每次获取就是从容器中拿
     * request:同一次请求创建一个实例
     * session:同一个session创建一个实例
     *
     * 懒加载：
     *      单实例bean：默认在容器启动时创建对象
     *       懒加载：容器启动不创建对象，第一次获取才创建 @Lazy注解
     */
    @Bean("person")
    @Scope("prototype")
    public Person person(){
        return new Person("zhangsan",25);
    }

    /**
     * @Conditional({Condition}):按照一定的条件判断，满足条件给容器中注册bean
     */
    @Conditional({WindowsCondition.class})
    @Bean("ywl")
    public Person person01(){
        return new Person("ywl",11);
    }

    @Conditional({LinuxCondition.class})
    @Bean("ljk")
    public Person person02(){
        return new Person("ljk",12);
    }


    @Bean
    public ColorFactory colorFactory(){
        return new ColorFactory();
    }
}
