package com.doopp.gauss.server.undertow;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.RedirectHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.ServletContainerInitializerInfo;
import io.undertow.servlet.handlers.DefaultServlet;
import io.undertow.servlet.util.ImmediateInstanceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import javax.servlet.ServletContainerInitializer;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class UndertowServer implements InitializingBean, DisposableBean {
    private final Logger logger = LoggerFactory.getLogger(UndertowServer.class);

    private String webAppName;
    private Resource webAppRoot;
    private int port = 8080;
    private ServletContainerInitializer servletContainerInitializer;

    private Undertow server;
    private DeploymentManager manager;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Starting Undertow web server on port {}, serving web application '{}' having root at {}", port, webAppName, webAppRoot.getFile().getAbsolutePath());

        InstanceFactory<? extends ServletContainerInitializer> instanceFactory = new ImmediateInstanceFactory<>(servletContainerInitializer);
        ServletContainerInitializerInfo sciInfo = new ServletContainerInitializerInfo(WebAppServletContainerInitializer.class, instanceFactory, new HashSet<>());

        DeploymentInfo deploymentInfo = constructDeploymentInfo(sciInfo);

        manager = Servlets.defaultContainer().addDeployment(deploymentInfo);
        // System.out.print("\n 2 >>> " + manager + "\n");
        manager.deploy();
        HttpHandler httpHandler = manager.start();

        PathHandler pathHandler = constructPathHandler(httpHandler);

        server = Undertow.builder()
            .addHttpListener(port, "localhost")
            .setHandler(pathHandler)
            .build();

        server.start();

        logger.info("Undertow web server started; web application available at http://localhost:{}/{}", port, webAppName);
    }

    private DeploymentInfo constructDeploymentInfo(ServletContainerInitializerInfo sciInfo) throws IOException {
        File webAppRootFile = webAppRoot.getFile();

        System.out.print(" >>> " + webAppRoot + "\n");
        System.out.print(" >>> " + webAppRootFile + "\n");

        return Servlets.deployment()
            .addServletContainerInitalizer(sciInfo)
            .setClassLoader(UndertowServer.class.getClassLoader())
            .setContextPath(webAppName)
            .setDeploymentName(webAppName + "-war")
            .setResourceManager(new FileResourceManager(webAppRootFile, 0))
            .addServlet(Servlets.servlet("default", DefaultServlet.class));
    }

    private PathHandler constructPathHandler(HttpHandler httpHandler) {
        RedirectHandler defaultHandler = Handlers.redirect("/" + webAppName);
        PathHandler pathHandler = Handlers.path(defaultHandler);
        pathHandler.addPrefixPath("/" + webAppName, httpHandler);
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
}
