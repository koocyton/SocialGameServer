package com.doopp.gauss.server.undertow;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class KTApplication {

    public static void main(String[] args) {

        // 有问题，找不到模版
        // final AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:config/spring-undertow.xml");
        // final ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:config/spring-undertow.xml");
        final AbstractApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:config/spring-undertow.xml");

        // add a shutdown hook for the above context...
        // 使用关闭钩子shutdownHook来进行销毁Bean
        ctx.registerShutdownHook();

        // ctx.getEnvironment();
        // UndertowServer undertowServer  =  (UndertowServer) ctx.getBean("undertowServer");
        // ctx.close();
    }
}
