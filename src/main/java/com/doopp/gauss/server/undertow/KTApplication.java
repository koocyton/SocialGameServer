package com.doopp.gauss.server.undertow;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class KTApplication {

    public static void main(String[] args) {

        // 有问题，找不到模版
        // final ApplicationContext context = new FileSystemXmlApplicationContext("classpath:config/spring-undertow.xml");
        // context.getBean("undertowServer");

        final ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:config/spring-undertow.xml");
        // context.getEnvironment();
        // UndertowServer undertowServer  =  (UndertowServer) context.getBean("undertowServer");

        // context.close();
    }
}
