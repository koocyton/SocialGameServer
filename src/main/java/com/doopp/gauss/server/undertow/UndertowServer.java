package com.doopp.gauss.server.undertow;

import com.doopp.gauss.server.KTApplication;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;

import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.RedirectHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.Resource;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.*;
import io.undertow.servlet.handlers.DefaultServlet;
import io.undertow.servlet.util.ImmediateInstanceFactory;

import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContainerInitializer;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class UndertowServer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String webAppName;
    private Resource webAppRoot;
    private int port = 8080;
    private ServletContainerInitializer servletContainerInitializer;
    // private WebSocketConnectionCallback socketConnectionCallback;

    private Undertow server;
    private DeploymentManager manager;

    public void afterPropertiesSet() throws Exception {
        // logger.info("Starting Undertow web server on port {}, serving web application '{}' having root at {}", port, webAppName, webAppRoot.getFile().getAbsolutePath());

        // web servlet
        InstanceFactory<? extends ServletContainerInitializer> instanceFactory = new ImmediateInstanceFactory<>(servletContainerInitializer);
        ServletContainerInitializerInfo sciInfo = new ServletContainerInitializerInfo(WebAppServletContainerInitializer.class, instanceFactory, new HashSet<>());
        // System.out.print("\n 2 >>> " + sciInfo + "\n");
        DeploymentInfo deploymentInfo = constructDeploymentInfo(sciInfo);

        // add webSocket
        final WebSocketDeploymentInfo wsInfo = new WebSocketDeploymentInfo();
        deploymentInfo.addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME, wsInfo);

        manager = Servlets.defaultContainer().addDeployment(deploymentInfo);
        manager.deploy();
        HttpHandler httpHandler = manager.start();

        PathHandler pathHandler = constructPathHandler(httpHandler);

        server = Undertow.builder()
            .addHttpListener(port, "localhost")
            .setHandler(pathHandler)
            .build();

        server.start();

        // logger.info("Undertow web server started; web application available at http://localhost:{}/{}", port, webAppName);
        logger.info("Undertow web server started; web application available at http://localhost:{}", port);
    }

    private DeploymentInfo constructDeploymentInfo(ServletContainerInitializerInfo sciInfo) throws IOException {

        File webAppRootFile = webAppRoot.getFile();
        return Servlets.deployment()
            .addServletContainerInitalizer(sciInfo)
            .setClassLoader(UndertowServer.class.getClassLoader())
            .setContextPath(webAppName)
            .setDeploymentName(webAppName + "-war")
            .setResourceManager(new FileResourceManager(webAppRootFile, 0))
            .addServlet(Servlets.servlet("default", DefaultServlet.class));
    }

    private PathHandler constructPathHandler(HttpHandler httpHandler) {
        // RedirectHandler defaultHandler = Handlers.redirect("/" + webAppName);
        RedirectHandler defaultHandler = Handlers.redirect("/");
        PathHandler pathHandler = Handlers.path(defaultHandler);
        // pathHandler.addPrefixPath("/" + webAppName, httpHandler);
        pathHandler.addPrefixPath("/", httpHandler);
        // pathHandler.addPrefixPath("/game-socket", websocket(socketConnectionCallback));
        return pathHandler;
    }

    @Override
    public void destroy() throws Exception {
        logger.info("Stopping Undertow web server on port " + port);
        server.stop();
        manager.stop();
        manager.undeploy();
        logger.info("Undertow web server on port " + port + " stopped");
    }

    public void setWebAppRoot(Resource webAppRoot) {
        this.webAppRoot = webAppRoot;
    }

    public void setWebAppName(String webAppName) {
        this.webAppName = webAppName;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setServletContainerInitializer(ServletContainerInitializer servletContainerInitializer) {
        this.servletContainerInitializer = servletContainerInitializer;
    }

    // public void setSocketConnectionCallback(WebSocketConnectionCallback socketConnectionCallback) {
    //     this.socketConnectionCallback = socketConnectionCallback;
    // }
}


public class UndertowServlet {

    /**
     * Initialize, configure and start a server implementation.
     *
     * @param contextPath
     * @param deploymentName
     * @param servletName
     * @param contextConfigLocation
     * @param mapping
     * @param host
     * @param port
     */
    public UndertowServlet(final String contextPath, final String deploymentName, final String servletName, final String contextConfigLocation, final String mapping, final String host, final Integer port) {


        try {

            final DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(KTApplication.class.getClassLoader())
                .setContextPath(contextPath)
                .setDeploymentName(deploymentName)
                .setMajorVersion(3)
                .setMinorVersion(0)
                .addInitParameter("contextConfigLocation", "classpath:config/spring/applicationContext.xml")
                .addListener(new ListenerInfo(MyServletContextListener.class))
                .addServlet(
                    servlet(servletName, DispatcherServlet.class)
                        .addInitParam("contextConfigLocation", "classpath:config/spring-mvc/mvc-dispatcher-servlet.xml")
                        .addMapping(mapping)
                        .setLoadOnStartup(1)
                        .setAsyncSupported(true));

            final DeploymentManager manager = defaultContainer().addDeployment(servletBuilder);
            manager.deploy();

            final Undertow server = Undertow.builder()
                .addHttpListener(port, host)
                // .addListener(port, host)
                .setHandler(manager.start())
                .build();

            server.start();
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }
}