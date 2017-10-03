package com.doopp.gauss.server.undertow;

import com.doopp.gauss.api.utils.SessionFilter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.EnumSet;
import java.util.Set;

public class WebAppServletContainerInitializer implements ServletContainerInitializer, ApplicationContextAware {

    private ApplicationContext applicationContext;


    @Override
    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {

        // set log4j
        ctx.setInitParameter("log4jConfigLocation", "classpath:config/log4j/log4j.properties");
        ctx.setInitParameter("log4jRefreshInterval", "6000");
        ctx.addListener(org.springframework.web.util.Log4jConfigListener.class);

        // session filter
        FilterRegistration.Dynamic sessionFilter = ctx.addFilter("sessionFilter", DelegatingFilterProxy.class);
        sessionFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");

        // load applicationContext
        XmlWebApplicationContext rootWebAppContext = new XmlWebApplicationContext();
        rootWebAppContext.setConfigLocation("classpath:config/spring/applicationContext.xml");
        rootWebAppContext.setParent(applicationContext);
        ctx.addListener(new ContextLoaderListener(rootWebAppContext));

        // set encode
        FilterRegistration.Dynamic encodingFilter = ctx.addFilter("encoding-filter", CharacterEncodingFilter.class);
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, "/*");

        // springSecurityFilterChain
        // FilterRegistration.Dynamic springSecurityFilterChain = ctx.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
        // springSecurityFilterChain.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, "admin");

        // set spring mvc servlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.setContextConfigLocation("classpath:config/spring-mvc/mvc-dispatcher-servlet.xml");
        ServletRegistration.Dynamic dispatcher = ctx.addServlet("mvc-dispatcher", dispatcherServlet);//DispatcherServlet.class);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/*");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}