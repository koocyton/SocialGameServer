package com.doopp.gauss.server.undertow;

import com.doopp.gauss.api.utils.SessionFilter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

public class WebAppServletContainerInitializer implements ServletContainerInitializer, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private Resource webAppRoot;

    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {

        ctx.setInitParameter("log4jConfigLocation", "classpath:config/log4j/log4j.properties");
        ctx.setInitParameter("log4jRefreshInterval", "6000");
        ctx.addListener(org.springframework.web.util.Log4jConfigListener.class);
        // ctx.addListener(org.springframework.web.util.WebAppRootListener.class);
        // classPath = webAppRoot.getFile();

        /*
             <filter>
        <filter-name>sessionFilter</filter-name>
        <filter-class>com.doopp.gauss.api.utils.SessionFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>sessionFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
         */

        FilterRegistration.Dynamic sessionFilter = ctx.addFilter("esessionFilter", SessionFilter.class);
        // encodingFilter.setInitParameter("encoding", "UTF-8");
        // encodingFilter.setInitParameter("forceEncoding", "true");
        sessionFilter.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, "/*");
        //ctx.addFilter("sessionFilter", SessionFilter.class);
        //ctx.addFilter("sessionFilter", "/*");

        // ServletContext servletContext = request.getSession().getServletContext();
        // ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);

        XmlWebApplicationContext rootWebAppContext = new XmlWebApplicationContext();
        rootWebAppContext.setConfigLocation("classpath:config/spring/applicationContext.xml");
        rootWebAppContext.setParent(applicationContext);
        ctx.addListener(new ContextLoaderListener(rootWebAppContext));

        FilterRegistration.Dynamic encodingFilter = ctx.addFilter("encoding-filter", CharacterEncodingFilter.class);
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, "/*");

        // FilterRegistration.Dynamic springSecurityFilterChain = ctx.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
        // springSecurityFilterChain.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, "admin");

        // System.out.print(" >>> " + ctx.getServletRegistration(""));

        // AbstractApplicationContext context = new ClassPathXmlApplicationContext("config/spring-undertow.xml");
        // DispatcherServlet dispatcherServlet  =  (DispatcherServlet) applicationContext.getBean("dispatcherServlet");
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextConfigLocation("classpath:config/spring-mvc/mvc-dispatcher-servlet.xml");
        ServletRegistration.Dynamic dispatcher = ctx.addServlet("mvc-dispatcher", dispatcherServlet);//DispatcherServlet.class);
        // dispatcher.setMultipartConfig("classpath:config/spring-mvc/mvc-dispatcher-servlet.xml");
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");

        // ServletRegistration.Dynamic dispatcher2 = ctx.addServlet("customer", DispatcherServlet.class);
        // dispatcher2.setLoadOnStartup(1);
        // dispatcher2.addMapping("/customer/*");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setWebAppRoot(Resource webAppRoot) {
        this.webAppRoot = webAppRoot;
    }
}