package com.npu.config;

import com.npu.aop.LogAspect;
import com.npu.aop.MathCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AOP:【动态代理】
 *      指在程序运行期间动态的将某段代码切入到指定方法指定位置进行运行的编程模式
 *  1、导入aop模块
 *  2、定义一个业务逻辑类（MathCalculator），在业务逻辑运行的时候将日志进行打印（方法之前，方法运行结束，方法出现异常等）
 *  3、定义一个日志切面类（LogAspect），切面类里面的方法需要动态感知MathCalculator.div运行到哪里，然后执行
 *      通知方法：
 *              前置通知(@Before)：logStart:在目标方法（div）运行之前运行 参数列表传入joinPoint可获取到方法的相关属性,且该参数必须放在第一个参数，否则无法识别
 *              后置通知(@After)：logEnd：在目标方法（div）运行之后运行,无论方法正常结束还是异常结束
 *              返回通知(@AfterReturning(returning可以指定封装返回值的参数）)：logReturn：在目标方法（div）正常返回之后运行
 *              异常通知(@AfterThrowing)：logException：在目标方法（div）出现异常以后运行
 *              环绕通知(@Around)：动态代理，手动推进目标方法运行（joinPoint.proceed())
 *  4、给切面类的目标方法标注何时何地运行（通知注解）
 *  5、将切面类和目标和业务逻辑类（目标方法所在类）都加入到容器中；
 *  6、告诉Spring哪个类是切面类（给切面类上加一个注解@Aspect）
 *  [7]、需要给配置类加一个@EnableAspectJAutoProxy【开启基于注解的aop模式】
 *      在spring中很多的@EnableXXX;
 *
 * 三步：
 *      1、将业务逻辑组件和切面类都加入到容器中，告诉spring哪个是切面类（@Aspect）
 *      2、在切面类上的每一个通知方法标注通知注解，告诉spring何时何地运行（切入点表达式）
 *      3、开启基于注解的aop模式@EnableAspectJAutoProxy
 **/
/**
 *
 * AOP原理：【看给容器中注册了什么组件，这个组件什么时候工作，包括这个组件工作时候的功能】@EnableAspectJAutoProxy
 * 1、@EnableAspectJAutoProxy是什么？
 *      @Import(AspectJAutoProxyRegistrar.class):给容器导入AspectJAutoProxyRegistrar.class
 *          利用AspectJAutoProxyRegistrar自定义给容器中注册bean
 *          第一步给容器中注册了"internalAutoProxyCreator"=AnnotationAwareAspectJAutoProxyCreator的bean（BeanDefinition），即bean的定义信息
 * 2、AnnotationAwareAspectJAutoProxyCreator:
 *      AnnotationAwareAspectJAutoProxyCreator->
 *          AspectJAwareAdvisorAutoProxyCreator->
 *              AbstractAdvisorAutoProxyCreator->
 *                  AbstractAutoProxyCreator extends ProxyProcessorSupport implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware
 *    关注后置处理器（在bean初始化完成前后做事情）（SmartInstantiationAwareBeanPostProcessor）、自动装配beanFactory（BeanFactoryAware）
 *
 *    //装备beanFactory的逻辑
 *    AbstractAutoProxyCreator.setBeanFactory()
 *    //后置处理器的逻辑
 *    AbstractAutoProxyCreator.postProcessBeforeInstantiation()
 *
 *    AbstractAdvisorAutoProxyCreator.setBeanFactory()重写了父类方法，会在方法里调一个initBeanFactory()方法
 *
 *    AspectJAwareAdvisorAutoProxyCreator
 *
 *    AnnotationAwareAspectJAutoProxyCreator.initBeanFactory(),即调用setBeanFactory时会调用这里的initBeanFactory()方法
 * 流程：
 *      1）传入主配置类，创建ioc容器
 *      2）注册配置类，调用refresh（）刷新容器
 *      3）registerBeanPostProcessors(beanFactory);注册bean的后置处理器来方便拦截bean的创建
 *              1、先获取ioc容器中已经定义了的需要创建对象的所有BeanPostProcessor beanFactory.getBeanNamesForType();
 *              2、给容器中加别的BeanPostProcessor beanFactory.addBeanPostProcessor();
 *              3、对实现了PriorityOrdered接口和Ordered接口以及其它的BeanPostProcessor以进行分类
 *              4、优先注册实现了PriorityOrdered接口的BeanPostProcessor ，其次注册实现了Ordered接口的BeanPostProcessor，最后注册其它的
 *              5、AnnotationAwareAspectJAutoProxyCreator实现了Ordered接口，注册这个BeanPostProcessor，实际上就是创建这个对象，保存在容器中
 *                     如何创建名为internalAutoProxyCreator的AnnotationAwareAspectJAutoProxyCreator对象
 *                     doCreateBean():
 *                     1、创建bean的实例
 *                     2、populateBean();给bean的各种属性赋值
 *                     3、initializeBean();初始化bean
 *                              1、invokeAwareMethods()；处理Aware接口的方法回掉（AnnotationAwareAspectJAutoProxyCreator实现了BeanFactoryAware接口，会在这里将beanFactory回调给该bean）
 *                              2、applyBeanPostProcessorsBeforeInitialization();执行后置处理器的BeforeInitialization
 *                              3、invokeInitMethods()；执行初始化方法
 *                              4、applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);；执行后置处理器的AfterInitialization
 *                     4、AnnotationAwareAspectJAutoProxyCreator创建成功，并且通过beanFactory创建了aspectJAdvisorFactory，再用aspectJAdvisorFactory创建了aspectJAdvisorsBuilder
 *              6、把AnnotationAwareAspectJAutoProxyCreator这个bean注册到BeanFactory中，beanFactory.addBeanPostProcessor(postProcessor);
 * ===============以上是创建和注册AnnotationAwareAspectJAutoProxyCreator的过程====================================================================
 *         AnnotationAwareAspectJAutoProxyCreator是InstantiationAwareBeanPostProcessor类型的后置处理器
 *      4）finishBeanFactoryInitialization(beanFactory);完成beanFactory初始化工作；创建剩余的单实例bean
 *          1、遍历获取容器中所有的Bean，依次创建对象
 *                getBean()->doGetBean()->getSingleton()
 *          2、创建bean【AnnotationAwareAspectJAutoProxyCreator会在所有bean创建之前有一个拦截，会调用 postProcessBeforeInstantiation方法】
 *              1、先从缓存中获取当前bean，如果能获取到，说明bean是之前被创建过的，直接使用，否则再创建，只要创建好的bean都会被缓存起来
 *              2、createBean（）；创建bean AnnotationAwareAspectJAutoProxyCreator会在任何bean创建之前先尝试返回bean的实例
 *                  【BeanPostProcessor是在Bean对象创建完成初始化前后调用的】
 *                  【InstantiationAwareBeanPostProcessor是在创建bean实例之前先尝试用后置处理器返回对象】
 *                  1、resolveBeforeInstantiation()；解析BeforeInstantiation，希望后置处理器在此能返回一个代理对象，如果能返回就使用该对象；
 *                          1）后置处理器先尝试返回对象 bean = applyBeanPostProcessorsBeforeInstantiation()；
 *                                  applyBeanPostProcessorsBeforeInstantiation（）方法中会拿到所有后置处理器，
 *                                  判断如果是InstantiationAwareBeanPostProcessor类型的，就执行postProcessBeforeInstantiation方法
 *                                          1）判断当前bean是否在advisedBeans（保存了所有需要增强的bean）中
 *                                          2）判断当前bean是否是基础类型的Advice、PointCut、Advisor、AopInfrastructureBean，或者是否是切面（@Aspect）
 *                                          3）是否需要跳过
 *                                              1、获取候选的增强器（切面里面的通知方法）【List<Advisor> candidateAdvisors】，
 *                                                  每一个封装的通知方法的增强器是InstantiationModelAwarePointcutAdvisor,
 *                                                  判断每个增强器是否是AspectJPointcutAdvisor类型的，如果是就返回true
 *                                              2、否则永远返回false
 *                          2）if (bean != null) bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);//这里即bean创建出来了需要进行一次后置处理
 *                    如果不能就往下走
 *                  2、doCreateBean();真正的去创建一个bean实例；和3.5的流程一样
 *
 *
 * 主配置类被拦截时会将切面类的属性的相关拦截器加入到AnnotationAwareAspectJAutoProxyCreator中
 * 1、MathCalculator：
 *     1、 MathCalculator调用postProcessBeforeInstantiation()方法时不会返回bean，即通过doCreateBean创建对象
 *     2、doCreateBean过程中：在applyBeanPostProcessorsAfterInitialization（）方法中调用postProcessAfterInitialization()方法
 *          return wrapIfNecessary(bean, beanName, cacheKey);//即如果需要的情况下包装bean
 *          1、获取当前bean的所有增强器（通知方法） Object[] specificInterceptors
 *              1、找到候选的所有增强器（找哪些通知方法是需要切入当前bean方法）
 *              2、获取到能在当前bean使用的增强器
 *              3、给增强器排序
 *          2、保存当前bean在advisedBeans中
 *          3、如果当前bean需要增强，创建当前bean的代理对象
 *              1、获取所有增强器（通知方法）
 *              2、保存到proxyFactory中
 *              3、创建代理对象：Spring自动决定；最后创建出com.npu.aop.MathCalculator@39a8312f
 *                  JdkDynamicAopProxy(config)；jdk动态代理；
 *                  ObjenesisCglibAopProxy(config);cglib动态代理
 *          4、给容器中返回当前组件使用cglib增强了的代理对象
 *          5、以后容器中获取到的就是这个组件的代理对象，执行目标方法的时候，代理对象就会执行通知方法的流程
 *      3、目标方法的执行
 *          容器中保存了组件的代理对象（cglib增强后的对象），这个对象里面保存了详细信息（比如增强器，目标对象，xxx）
 *          1、CglibAopProxy.intercept();拦截目标方法的执行
 *          2、根据ProxyFactory对象获取目标方法将要执行的拦截器链getInterceptorsAndDynamicInterceptionAdvice()
 *                  1、创建一个List<Object> interceptorList = new ArrayList<>(advisors.length);保存所有拦截器链
 *                     有一个默认的ExposeInvocationInterceptor.ADVISOR和自己配置的四个InstantiationModelAwarePointcutAdvisor
 *                  2、遍历所有增强器，将其转为MethodInterceptor
 *                      registry.getInterceptors(advisor)：1、如果是 MethodInterceptor，直接加入到List中返回
 *                                                         2、如果不是，使用AdvisorAdapter将增强器转为MethodInterceptor
 *                                                                  三类适配器：1、MethodBeforeAdviceAdapter
 *                                                                             2、AfterReturningAdviceAdapter
 *                                                                             3、ThrowAdviceAdapter
 *                                                         3、转换完成，返回MethodInterceptor数组,即拦截器链
 *                                                              拦截器链：每一个通知方法又被包装成拦截器链，利用MethodInterceptor机制
 *          3、如果没有拦截器链，直接执行目标方法
 *          4、如果有拦截器链，把需要执行的目标对象，目标方法，拦截器链等信息传入创建一个CglibMethodInvocation对象，并调用proceed()方法获取返回值。
 *          5、拦截器链的触发过程(proceed()的方法中），拦截器链的机制保证通知方法与目标方法的执行顺序
 *                  1、如果没有拦截器，或者拦截器的索引和拦截器数组-1大小一样（执行到最后一个拦截器），直接执行目标方法后返回;
 *                      currentInterceptorIndex=this.interceptorsAndDynamicMethodMatchers.size() - 1
 *                  2、获取到当前下标拦截器interceptorOrInterceptionAdvice =this.interceptorsAndDynamicMethodMatchers.get(++this.currentInterceptorIndex)（将当前拦截器的索引+1）
 *                  3、调用((MethodInterceptor) interceptorOrInterceptionAdvice）.invoke(this);
 *                      this为cglib的代理对象，再调用proceed，直到MethodBeforeAdviceInterceptor时会调用前置通知，然后再调用proceed。
 *                  4、MethodBeforeAdviceInterceptor方法返回后执行目标方法后到AspectJAfterAdvice的proceed中，执行后置通知
 *                  5、AspectJAfterAdvice执行完成后又返回到AfterReturningAdviceInterceptor方法中，
 *                      执行afterReturning()方法（方法执行过程中间不出任何异常，如果出了异常会被上层AspectJAfterThrowingAdvice捕获）
 *                  6、如果出现异常AspectJAfterThrowingAdvice会捕获到执行invokeAdviceMethod（）方法
 *                  7、随后回到最初的proceed方法，返回，过程中一共进行了5次压栈操作、
 * 总结：
 *      1、@EnableAspectJAutoProxy 开启AOP功能
 *      2、@EnableAspectJAutoProxy会注册一个AnnotationAwareAspectJAutoProxyCreator组件
 *      3、AnnotationAwareAspectJAutoProxyCreator是一个后置处理器
 *      4、容器创建流程：
 *              1：registerBeanPostProcessor()注册所有后置处理器，会创建AnnotationAwareAspectJAutoProxyCreator对象
 *              2：finishBeanFactoryInitialize()初始化剩下的单实例bean
 *                  1）创建业务逻辑组件和切面组件
 *                  2）AnnotationAwareAspectJAutoProxyCreator会拦截组件的创建过程
 *                  3）组件创建完成之后，判断组件是否需要增强
 *                      是：切面的通知方法包装成增强器（Advisor）；给业务逻辑组件创建一个代理对象（cglib代理）
 *      5、执行目标方法：
 *          1、代理对象执行目标方法
 *          2、CglibProxy.intercept()
 *              1、得到目标方法的拦截器链（增强器包装成拦截器MethodInterceptor）
 *              2、利用拦截器的链式机制，依次进入每一个拦截器进行执行；
 *              3、效果：
 *                  正常执行：前置通知->目标方法->后置通知->返回通知
 *                  异常执行：前置通知->目标方法->后置通知->异常通知
 */
@EnableAspectJAutoProxy
@Configuration
public class MainConfigOfAOP {

    //业务逻辑类加入容器中
    @Bean
    public MathCalculator calculator(){
        return new MathCalculator();
    }

    //切面类加入容器中
    @Bean
    public LogAspect logAspect(){
        return new LogAspect();
    }

}
