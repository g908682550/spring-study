package com.npu.aop;

import org.springframework.stereotype.Component;

@Component
public class MathCalculator {

    public int div(int i,int j){
        System.out.println("本方法被调用");
        return i/j;
    }

}
