# Spring

## 组件注册

```java
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
 * @Conditional({Condition}):按照一定的条件判断，满足条件给容器中注册bean
 * @Scope
 * prototype:多例的 ioc容器启动并不会去调用方法创建对象在容器中，而是每次获取时才会调用方法创建对象
 * singleton:单例的（默认值） ioc容器启动会调用方法创建对象放到ioc容器中，以后每次获取就是从容器中拿
 */
```

```java
组件注册时过滤条件：

@ComponentScan value:指定要扫描的包

excludeFilters=Filter[]:指定扫描包的时候按照什么规则排除哪些组件

includeFilters=Filter[]:指定扫描包的时候要包含哪些组件,需将useDefaultFilters置false

FilterType.ANNOTATION:按照注解

FilterType.ASSIGNABLE_TYPE:按照指定的类型

FilterType.REGEX:使用正则指定

FilterType.CUSTOM:使用自定义规则
```



## 生命周期

~~~java
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
~~~

## 属性赋值

```java
//@Value赋值
//1、基本数值
//2、可以写SpEL;#{}
//3、可以写${}取出配置文件【properties】的值（在运行环境变量里面的值）
//使用@PropertySource读取外部配置文件中的k/v保存到运行的环境变量中;加载完外部的配置文件后使用${}取出配置文件的值
```

## 自动装配

~~~java
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
 *              可以和@Autowired一样实现自动装配功能；默认是按照组件名称进行装配的；没有能支持					@Primary的功能以及@Autowired（required=false）的功能
 *          2、@Inject（需要导入依赖）：
 *              导入javax.inject的包，和Autowired的功能一样，没有required=false的功能
 *
 *      AutowiredAnnotationBeanPostProcessor：解析完成自动装配功能
 *
 *      3、@Autowired：构造器，参数，方法，属性
 *        1）标注在方法位置  	标注在方法，Spring容器创建当前对象，就会调用方法，完成赋值，方法使用							  的参数，自定义类型的值从ioc容器中获取,@Bean标注的方法创建对象的时								     候，方法参数的值默认从ioc容器中获取，默认不写Autowired，效果是一样的
 *        2）标注在构造器位置   默认加在ioc容器中的组件，容器启动会调用无参构造器创建对象，再进行初始								 化赋值等操作。标注在构造器上可以默认调用该方法，方法中用的参数同样从								 ioc容器中获取，如果容器只有一个有参构造器，这个有参构造器的Autowired							  可以省略，参数位置的组件还是可以自动从容器中获取
 *        3）标注在参数位置     从ioc容器中获取参数组件的值
 
 *      4、自定义组件想要使用Spring容器底层的一些组件（ApplicationContext，BeanFactory，xxx）;
 *           自定义组件需要实现xxxAware接口；在创建对象的时候会调用接口规定的方法注入相关组件；
 *           把Spring底层的一些组件注入到自定义的bean中
 *           xxxAware的功能都是使用xxxAwareProcessor处理的
 *
 */
~~~

~~~java
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
~~~

## AOP

### AOP的使用

~~~java
/**
 * AOP:【动态代理】
 *      指在程序运行期间动态的将某段代码切入到指定方法指定位置进行运行的编程模式
 *  1、导入aop模块
 *  2、定义一个业务逻辑类（MathCalculator），在业务逻辑运行的时候将日志进行打印（方法之前，方法运行结	   束，方法出现异常等）
 *  3、定义一个日志切面类（LogAspect），切面类里面的方法需要动态感知MathCalculator.div运行到哪里，然		 后执行
 *      通知方法：
 *              前置通知(@Before)：logStart:在目标方法（div）运行之前运行 参数列表传入joinPoint可				获取到方法的相关属性,且该参数必须放在第一个参数，否则无法识别
 
 *              后置通知(@After)：logEnd：在目标方法（div）运行之后运行,无论方法正常结束还是异常结束
 *              
 				返回通知(@AfterReturning(returning可以指定封装返回值的参数）)：logReturn：在目标				  方法（div）正常返回之后运行
 				
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
 */
~~~

### AOP的原理

~~~java
/**
 *
AOP原理：【看给容器中注册了什么组件，这个组件什么时候工作，包括这个组件工作时候的功能】		@EnableAspectJAutoProxy
 * 1、@EnableAspectJAutoProxy是什么？
 *      @Import(AspectJAutoProxyRegistrar.class):给容器导入AspectJAutoProxyRegistrar.class
 *          利用AspectJAutoProxyRegistrar自定义给容器中注册bean
 *          第一步给容器中注册了"internalAutoProxyCreator"=AnnotationAwareAspectJAutoProxyCreator的bean（BeanDefinition），即bean的定义信息
 * 2、AnnotationAwareAspectJAutoProxyCreator extends
 *      AnnotationAwareAspectJAutoProxyCreator extends
 *          AspectJAwareAdvisorAutoProxyCreator extends
 *              AbstractAdvisorAutoProxyCreator  extends
 *                  AbstractAutoProxyCreator extends ProxyProcessorSupport implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware
	关注后置处理器（在bean初始化完成前后做事情）（SmartInstantiationAwareBeanPostProcessor）、自动		装配beanFactory（BeanFactoryAware）
 *    装备beanFactory的逻辑
 *    AbstractAutoProxyCreator.setBeanFactory()
 *    后置处理器的逻辑
 *    AbstractAutoProxyCreator.postProcessBeforeInstantiation()
 *    AbstractAdvisorAutoProxyCreator.setBeanFactory()重写了父类方法，会在方法里调一个initBeanFactory()方法
 *    AspectJAwareAdvisorAutoProxyCreator
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
~~~

proceed方法的过程

<https://github.com/g908682550/spring-study/blob/master/src/main/resources/img/1579231689471.png>

## 声明式事务

~~~java
/**
 * 声明式事务：
 *
 * 环境搭建：
 *      1、导入相关依赖
 *          数据源、数据库、SpringJdbc模块
 *      2、配置数据源、JdbcTemplate操作数据
 *      3、给方法上面标注@Transactional 标识当前方法是一个事务方法
 *      4、@EnableTransactionManagement开启基于注解的事务管理功能
 *      5、配置事务管理器来控制事务 public PlatformTransactionManager platformTransactionManager
 *原理：
 *      1、@EnableTransactionManagement利用
 *                  TransactionManagementConfigurationSelector给容器中导入两个组件
 *                  1、AutoProxyRegistrar，它会给容器中注册一个InfrastructureAdvisorAutoProxyCreator组件
 *                                         InfrastructureAdvisorAutoProxyCreator也是一个后置处理器，利用后置处理器机制在对象创建以后包装对象，返回一个代理对象（增强器），
 *                                         代理对象执行方法利用拦截器链进行调用
 *                  2、ProxyTransactionManagementConfiguration
 *                          1、它会给容器中注册事务增强器、
 *                                  1、事务增强器要用事务注解的信息，AnnotationTransactionAttributeSource解析事务注解
 *                                  2、事务增强器需要事务拦截器：TransactionInterceptor保存了事务的属性信息，事务管理器
 *                                      TransactionInterceptor（它是一个MethodIntercepter）在目标方法执行的时候执行拦截器链
 *                                          事务拦截器：
 *                                              1、先获取事务相关的属性
 *                                              2、在获取PlatformTransactionManager，没有事先没有指定，最终会从容器中按照类型获取一个TransactionManager
 *                                              3、执行目标方法，如果异常，获取到事务管理器，利用事务管理器回滚操作，如果正常，利用事务管理器提交事务
 */
~~~



## 扩展原理

~~~java
/**
 * 扩展原理
 * BeanPostProcessor：bean后置处理器，bean创建对象初始化前后进行拦截工作的
 * 1、BeanFactoryPostProcessor：beanFactory的后置处理器，在beanFactory标注初始化后调用，所以bean的定义已经保存加载到beanFactory，但是bean的实例还未创建
 *      1、ioc容器创建对象
 *      2、执行invokeBeanFactoryPostProcessors(beanFactory);执行BeanFactoryPostProcessors
 *          如何找到所有的BeanFactoryPostProcessor并执行它们的方法：
 *              1、String[] postProcessorNames =beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
 *              2、在初始化创建其它组件前面执行
 *
 * 2、BeanDefinitionRegistryPostProcessor是BeanFactoryPostProcessor的子接口，BeanDefinitionRegistry是Bean定义信息的保存中心，BeanFactory就是按照其中保存的bean的定义信息创建bean实例的
 *      postProcessBeanDefinitionRegistry()方法，在所有bean定义信息将要被加载到，但是bean实例还未创建，优先于BeanFactoryPostProcess执行，可以利用其给容器中再来添加一些组件
 * 原理：
 *      1）、ioc容器创建对象
 *      2）、执行执行invokeBeanFactoryPostProcessors(beanFactory);
 *      3）、从容器中获取到所有的BeanDefinitionRegistryPostProcessor组件beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
 *          1、先触发 postProcessBeanDefinitionRegistry（）方法
 *          2、再触发 postProcessBeanFactory（）方法
 *      4）、再来从容器中执行BeanFactoryPostProcessor类型的组件，然后依次触发postProcessBeanFactory（）方法
 *
 * 3、ApplicationListener:监听容器中发布的事件，事件驱动模型的开发
 *      ApplicationListener<E extends ApplicationEvent>
 *      监听ApplicationEvent及其子类的相关事件
 *   步骤：
 *      1）、写一个监听器来监听某个事件（ApplicationEvent及其子类）
 *          @EventListener(class={})可以在普通的业务逻辑组件上的方法监听事件
 *          原理：使用EventListenerMethodProcessor处理器来解析方法上的@EventListener注解，它实现了EventListenerMethodProcessor接口
 *                  SmartInitializingSingleton接口的原理：单实例bean全部创建完成后
 *                  1）ioc容器创建,refresh（）;
 *                  2）finishBeanFactoryInitialization(beanFactory);初始化剩下的单实例bean；
 *                      1）一顿遍历先创建所有的单实例bean；
 *                      2）获取有创建好的单实例bean，判断是否是实现了 SmartInitializingSingleton接口类型的，
 *                          如果是就调用该接口的afterSingletonsInstantiated()方法
 *      2）、把监听器加入到容器中
 *      3）、只要容器中有相关类型的事件的发布，就能监听到这个事件
 *              ContextRefreshedEvent：容器刷新完成（所有bean都完全创建）会发布这个事件
 *              ContextClosedEvent：关闭容器发布这个事件
 *      4）、自定义发布一个事件 ioc容器.publishEvent(ApplicationEvent);
 *
 *    原理：
 *       ContextRefreshedEvent、IOCTest_Ext$1、ContextClosedEvent
 *       1、ContextRefreshedEvent事件：
 *          1）容器创建对象：refresh（）;
 *          2）finishRefresh（）方法中调用publishEvent(new ContextRefreshedEvent(this));
 *      2、自己发布的事件 publishEvent();
 *      3、ContextClosedEvent:close方法调用doClose方法发布ContextClosedEvent事件
 *
 *       【事件发布流程】即publishEvent方法：
 *           1、获取事件的多播器：getApplicationEventMulticaster();
 *           2、调用multicastEvent(applicationEvent, eventType)派发事件
 *           3、获取到所有的ApplicationListener,即getApplicationListeners()
 *                1、如果有Executor，可以支持使用Executor进行异步派发
 *                2、否则同步的方式直接执行invokeListener(listener, event);
 *               拿到listener回调onApplicationEvent方法
 *        【事件的多播器【派发器】】
 *           1、容器创建对象：refresh（）中
 *           2、initApplicationEventMulticaster();会初始化多播器
 *                  1、先去容器中有没有id="applicationEventMulticaster"的组件
 *                  2、如果没有，new SimpleApplicationEventMulticaster(beanFactory);同时注册到容器中，我们就可以在其它组件要派发事件，自动注入这个派发器
 *        【容器中有哪些监听器】
 *           1、容器创建对象：refresh（）中
 *           2、registerListeners();
 *              从容器中拿到所有的监听器，把他们注册到applicationEventMulticaster中；
 *              String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
 *              //将listener注册到多播器中
 *              for (String listenerBeanName : listenerBeanNames)
 * 			        getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
 *
 */
~~~

