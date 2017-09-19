//package com.doopp.gauss.server.netty;
//
//import org.springframework.beans.BeansException;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.buffer.PooledByteBufAllocator;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//
//public class HttpServer implements Server {
//
//    private static int servicePort;
//
//    private static final EventLoopGroup bossGroup = new NioEventLoopGroup();
//    private static final EventLoopGroup workGroup = new NioEventLoopGroup();
//
//    public HttpServer(int port) {
//        servicePort = port;
//    }
//
//    public void init(String spring,String _package) throws BeansException, ClassNotFoundException {
//        new SpringContext().init(spring,_package);
//    }
//
//    public void start() throws Exception {
//
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workGroup)
//                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
//                .childOption(ChannelOption.TCP_NODELAY, true)
//                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
//                .channel(NioServerSocketChannel.class)
//                .childHandler(new MyChannelInitializer());
//
//            // 绑定端口，同步等待成功
//            ChannelFuture sync = b.bind(servicePort).sync();
//            System.out.println("socket server started at port " + servicePort + '.');
//
//            //等待服务端监听端口关闭
//            sync.channel().closeFuture().sync();
//
//        } finally {
//
//            bossGroup.shutdownGracefully();
//            workGroup.shutdownGracefully();
//
//        }
//    }
//
//    public void stop() {
//        bossGroup.shutdownGracefully();
//        workGroup.shutdownGracefully();
//    }
//
//    public void status() {
//    }
//}
