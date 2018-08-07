package com.hr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableZuulProxy
@ComponentScan
@MapperScan(basePackages= {"com.hr.mapper"})
@EnableTransactionManagement//开启事务
public class ZuulApplication {
    public static void main(String[] args) {
    	SpringApplication.run(ZuulApplication.class, args);
    }
}