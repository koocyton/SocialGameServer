//package com.doopp.gauss.server.netty;
//
//import java.io.UnsupportedEncodingException;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
//import io.netty.buffer.Unpooled;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.handler.codec.http.DefaultFullHttpResponse;
//import io.netty.handler.codec.http.FullHttpRequest;
//import io.netty.handler.codec.http.FullHttpResponse;
//import io.netty.handler.codec.http.HttpMethod;
//import io.netty.handler.codec.http.HttpObject;
//import io.netty.handler.codec.http.HttpResponseStatus;
//import io.netty.handler.codec.http.HttpVersion;
//
//public class MyChannelHandler extends SimpleChannelInboundHandler<HttpObject> {
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
//        handleHttpRequest(ctx, (FullHttpRequest)msg);
//    }
//
//    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) throws Exception {
//
//
//        FullHttpResponse res = null;
//
//        try {
//            if(req.decoderResult().isFailure()) {
//                Writer.writeAndFlush(ctx, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
//                return;
//            }
//
//            if (!req.method().equals(HttpMethod.GET) && !req.method().equals(HttpMethod.POST)) {
//                Writer.writeAndFlush(ctx, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.METHOD_NOT_ALLOWED));
//                return;
//            }
//
//            String path = req.uri();
//
//            if(path.indexOf("?") != -1) {
//                path = path.substring(0, req.uri().indexOf("?"));
//            }
//
//            if ("/".equals(path)) {
//                res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
//                    io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND);
//                Writer.writeAndFlush(ctx, res);
//                return;
//            }
//
//            if ("/favicon.ico".equals(path)) {
//                res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
//                    io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND);
//                Writer.writeAndFlush(ctx, res);
//                return;
//            }
//
//            SpringContext.InvokeObj io = SpringContext.getBean(path,req.method());
//
//            if(io == null) {
//                res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
//                    HttpResponseStatus.METHOD_NOT_ALLOWED);
//                Writer.writeAndFlush(ctx, res);
//                return;
//            }
//
//            //解析参数
//            ArgsHolder args = new ArgsParser(req).parse();
//
//            Object result = invokeService(io,ctx,args);
//
//            if(result == null) {
//                return;
//            }
//
//            res = getFullHttpResponse(result);
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.SERVICE_UNAVAILABLE,
//                Unpooled.wrappedBuffer("service unavailable".getBytes()));
//
//        }
//
//        Writer.writeAndFlush(ctx, res);
//    }
//
//    private Object invokeService(SpringContext.InvokeObj io,ChannelHandlerContext ctx,ArgsHolder args) throws IllegalAccessException, InvocationTargetException {
//
//        Method mtd = io.getMtd();
//        Object obj = io.getObj();
//        Object result = mtd.invoke(obj,ctx,args);
//
//        return result;
//    }
//
//    private FullHttpResponse getFullHttpResponse(Object result) throws UnsupportedEncodingException {
//
//        FullHttpResponse res = null;
//
//        if(result instanceof byte[]) {
//            res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK
//                ,Unpooled.wrappedBuffer((byte[])result));
//        } else {
//            res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK
//                ,Unpooled.wrappedBuffer(JsonUtil.obj2json(result).getBytes("utf-8")));
//        }
//
//        return res;
//    }
//
//}
