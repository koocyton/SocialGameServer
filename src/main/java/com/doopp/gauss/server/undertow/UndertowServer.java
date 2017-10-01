package com.doopp.gauss.server.undertow;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.server.HttpHandler;

import static io.undertow.Handlers.path;

import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.RedirectHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.InstanceFactory;
import io.undertow.servlet.api.ServletContainerInitializerInfo;
import io.undertow.servlet.handlers.DefaultServlet;
import io.undertow.servlet.util.ImmediateInstanceFactory;

import static io.undertow.Handlers.resource;
import static io.undertow.Handlers.websocket;

import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import io.undertow.websockets.spi.WebSocketHttpExchange;
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

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String webAppName;
    private Resource webAppRoot;
    private int port = 8080;
    private ServletContainerInitializer servletContainerInitializer;

    private Undertow server;
    private DeploymentManager manager;

    @Override
    public void afterPropertiesSet() throws Exception {
        // logger.info("Starting Undertow web server on port {}, serving web application '{}' having root at {}", port, webAppName, webAppRoot.getFile().getAbsolutePath());

        InstanceFactory<? extends ServletContainerInitializer> instanceFactory = new ImmediateInstanceFactory<>(servletContainerInitializer);
        ServletContainerInitializerInfo sciInfo = new ServletContainerInitializerInfo(WebAppServletContainerInitializer.class, instanceFactory, new HashSet<>());
        // System.out.print("\n 2 >>> " + sciInfo + "\n");
        DeploymentInfo deploymentInfo = constructDeploymentInfo(sciInfo);

        manager = Servlets.defaultContainer().addDeployment(deploymentInfo);
        manager.deploy();
        HttpHandler httpHandler = manager.start();

        HttpHandler webSocketHandle = this.webSocketHandler();

        PathHandler pathHandler = constructPathHandler(httpHandler, webSocketHandle);

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
        // System.out.print(" >>> " + webAppRoot + "\n");
        // System.out.print(" >>> " + webAppRootFile + "\n");

        return Servlets.deployment()
            .addServletContainerInitalizer(sciInfo)
            .setClassLoader(UndertowServer.class.getClassLoader())
            .setContextPath(webAppName)
            .setDeploymentName(webAppName + "-war")
            .setResourceManager(new FileResourceManager(webAppRootFile, 0))
            .addServlet(Servlets.servlet("default", DefaultServlet.class));
        //.addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME,
        //new WebSocketDeploymentInfo()
        //    .setBuffers(new DefaultByteBufferPool(true, 100))
        //    .addEndpoint(ChatEndpoint.class));
    }

    // private DeploymentInfo

    private PathHandler constructPathHandler(HttpHandler httpHandler, HttpHandler webSocketHandler) {
        // RedirectHandler defaultHandler = Handlers.redirect("/" + webAppName);
        RedirectHandler defaultHandler = Handlers.redirect("/");
        PathHandler pathHandler = Handlers.path(defaultHandler);
        // pathHandler.addPrefixPath("/" + webAppName, httpHandler);
        pathHandler.addPrefixPath("/", httpHandler);
        pathHandler.addPrefixPath("/game-socket", webSocketHandler);
        return pathHandler;
    }

    private HttpHandler webSocketHandler() {

        return websocket(new WebSocketConnectionCallback() {

            @Override
            public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
                channel.getReceiveSetter().set(new AbstractReceiveListener() {

                    @Override
                    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
                        logger.info( " >>> message " + message.getData());
                        logger.info( " >>> channel " + channel.getUrl());
                        WebSockets.sendText(message.getData(), channel, null);
                    }
                });
                channel.resumeReceives();
            }
        });
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
