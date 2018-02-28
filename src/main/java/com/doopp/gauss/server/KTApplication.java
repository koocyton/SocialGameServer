package com.doopp.gauss.server;

import com.doopp.gauss.server.undertow.GuessDrawGame;
import com.doopp.gauss.server.undertow.UndertowServer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.undertow.server.handlers.resource.PathResource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class KTApplication {

    public static void main(String[] args) {

        public static void main(String[] args) throws Exception {
            String propertiesConfig = args[0];
            Injector injector = Guice.createInjector(new NettyModule(propertiesConfig));
            final UndertowServer server = injector.getInstance(UndertowServer.class);
            server.run();
        }
    }
}
