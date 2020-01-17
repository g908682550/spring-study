package com.npu.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * 切面类
 */
@Aspect
public class LogAspect {

    //抽取公共的切入点表达式
    //1、本类引用
    @Pointcut("execution(public int com.npu.aop.MathCalculator.*(..))")
    public void pointCut(){}

    //在目标方法之前切入：切入点表达式（指定在哪个方法切入）
    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint){
        System.out.println(""+joinPoint.getSignature().getName()+"除法运行。。。参数列表是：{"+ Arrays.asList(joinPoint.getArgs())+"}");
    }

    @After("pointCut()")
    public void logEnd(){
        System.out.println("除法运行结束。。。");
    }

    @AfterReturning(value = "pointCut()",returning ="result" )
    public void logReturn(Object result){
        System.out.println("除法正常返回。。运行结果：{"+result+"}");
    }

    @AfterThrowing("pointCut()")
    public void LogException(){
        System.out.println("除法异常。。异常信息：{}");
    }

}
