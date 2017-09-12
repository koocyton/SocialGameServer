package com.doopp.gauss.server;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class KTSpringApplication {

    public static void main(String[] args) {

        AbstractApplicationContext context = new ClassPathXmlApplicationContext("config/spring-undertow.xml");

        UndertowServer undertowServer  =  (UndertowServer) context.getBean("undertowServer");

        // context.registerShutdownHook();
        context.close();
    }
}
