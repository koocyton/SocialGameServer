//package com.doopp.gauss.server.netty;
//
//import org.sharegpj.handler.MyChannelHandler;
//
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.handler.codec.http.HttpObjectAggregator;
//import io.netty.handler.codec.http.HttpRequestDecoder;
//import io.netty.handler.codec.http.HttpResponseEncoder;
//
//public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {
//
//    @Override
//    public void initChannel(SocketChannel ch) throws Exception {
//
//        ChannelPipeline pipeline = ch.pipeline();
//
//        pipeline.addLast(new HttpResponseEncoder());
//        pipeline.addLast(new HttpRequestDecoder());
//        pipeline.addLast("aggregator", new HttpObjectAggregator(1048500));
//        pipeline.addLast("socket", new MyChannelHandler());
//    }
//}
