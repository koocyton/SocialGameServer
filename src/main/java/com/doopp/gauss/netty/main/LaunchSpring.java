package com.doopp.gauss.netty.main;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LaunchSpring  {

    private static Logger log = Logger.getLogger(LaunchSpring.class);

    public static void main(String[] args) {
        // 加载sping配置文件
        ApplicationContext context = new ClassPathXmlApplicationContext("config/spring/applicationContext.xml");
    }
}
