package com.doopp.gauss.server.undertow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class KTApplication {

    public static void main(String[] args) {

        // ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("file:" + appPath + "/config/rootContext.xml");

        AbstractApplicationContext context = new ClassPathXmlApplicationContext("config/spring-undertow.xml");

        UndertowServer undertowServer  =  (UndertowServer) context.getBean("undertowServer");

        // context.registerShutdownHook();
        // context.close();
    }
}
