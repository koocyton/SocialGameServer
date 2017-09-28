//package com.doopp.gauss.server.netty2;
//
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.Channel;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.net.ssl.SSLContext;
//
//public class KTApplication
//{
//    private static final Logger logger = LoggerFactory.getLogger(KTApplication.class);
//
//    public static final boolean SSL = System.getProperty("ssl") != null;
//    static final int PORT = Integer.parseInt(System.getProperty("port", SSL? "8443" : "8080"));
//
//    @Autowired
//    WebSocketServerInitializer webSocketServerInitializer;
//
//    public void init() throws InterruptedException {
//        // Configure SSL.
//        final SSLContext sslCtx =  null;
//        EventLoopGroup bossGroup = new NioEventLoopGroup(4);
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup)
//                .channel(NioServerSocketChannel.class)
//                .childHandler(webSocketServerInitializer);
//
//            Channel ch = b.bind(PORT).sync().channel();
//
//            System.out.println("Open your web browser and navigate to " +
//                (SSL? "https" : "http") + "://127.0.0.1:" + PORT + '/');
//
//            ch.closeFuture().sync();
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
//    }
//
//    public static void main(String[] args) throws InterruptedException {
//        String[] configurations = { "config/applicationContext.xml","config/applicationContext-netty.xml"};
//
//        ClassPathXmlApplicationContext context=new ClassPathXmlApplicationContext(configurations);
//
//        logger.info("-------------------------------------------");
//        logger.info("start nettyContainer....");
//        logger.info("--------------------------------------------");
//    }
//}
