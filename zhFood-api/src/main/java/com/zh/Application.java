package com.zh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication

@MapperScan(basePackages = "com.zh.mapper")//扫描mabaitis下的mapper包
// 扫描所有包以及相关组件包
@ComponentScan(basePackages = {"com.zh", "org.n3r.idworker"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
