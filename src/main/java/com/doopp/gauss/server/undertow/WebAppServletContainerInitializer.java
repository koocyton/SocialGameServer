//package com.doopp.gauss.server.undertow;
//
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.web.context.ContextLoaderListener;
//import org.springframework.web.context.support.XmlWebApplicationContext;
//import org.springframework.web.filter.CharacterEncodingFilter;
//import org.springframework.web.filter.DelegatingFilterProxy;
//import org.springframework.web.servlet.DispatcherServlet;
//
//import javax.servlet.*;
//import java.util.EnumSet;
//import java.util.Set;
//
//public class WebAppServletContainerInitializer implements ServletContainerInitializer, ApplicationContextAware {
//
//    private ApplicationContext applicationContext;
//
//    @Override
//    public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
//        XmlWebApplicationContext rootWebAppContext = new XmlWebApplicationContext();
//        rootWebAppContext.setConfigLocation("config/spring/applicationContext.xml");
//        rootWebAppContext.setParent(applicationContext);
//        ctx.addListener(new ContextLoaderListener(rootWebAppContext));
//
//        FilterRegistration.Dynamic encodingFilter = ctx.addFilter("encoding-filter", CharacterEncodingFilter.class);
//        encodingFilter.setInitParameter("encoding", "UTF-8");
//        encodingFilter.setInitParameter("forceEncoding", "true");
//        encodingFilter.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, "admin");
//
//        FilterRegistration.Dynamic springSecurityFilterChain = ctx.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
//        springSecurityFilterChain.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, "admin");
//
//        ServletRegistration.Dynamic dispatcher = ctx.addServlet("admin", DispatcherServlet.class);
//        dispatcher.setLoadOnStartup(1);
//        dispatcher.addMapping("/admin/*");
//
//        ServletRegistration.Dynamic dispatcher2 = ctx.addServlet("customer", DispatcherServlet.class);
//        dispatcher2.setLoadOnStartup(1);
//        dispatcher2.addMapping("/customer/*");
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
//}
