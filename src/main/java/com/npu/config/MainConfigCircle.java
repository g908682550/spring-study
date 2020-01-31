package com.npu.config;

import com.npu.bean.Blue;
import com.npu.bean.Boss;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({Blue.class, Boss.class})
public class MainConfigCircle {

}
