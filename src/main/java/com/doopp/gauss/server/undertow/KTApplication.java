package com.doopp.gauss.server.undertow;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class KTApplication {

    public static void main(String[] args) {

        final ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:config/spring-undertow.xml");

        // final ApplicationContext context = new ClassPathXmlApplicationContext("file:" + appPath + "/config/rootContext.xml");

        // AbstractApplicationContext context = new ClassPathXmlApplicationContext("config/spring-undertow.xml");

        // UndertowServlet undertowServlet  =  (UndertowServlet) context.getBean("undertowServlet");

        // context.registerShutdownHook();
        // context.close();
    }
}
