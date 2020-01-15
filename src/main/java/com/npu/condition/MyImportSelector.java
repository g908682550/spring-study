package com.npu.condition;

import com.npu.bean.Blue;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

//自定义逻辑需要返回的容器组件
public class MyImportSelector implements ImportSelector {

    //返回值，就是容器中要导入到容器中的组件全类名
    //AnnotationMetadata：当前标注@Import注解的类的所有注解信息
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"com.npu.bean.Blue"};
    }
}
