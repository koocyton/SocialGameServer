//package com.doopp.gauss.server.netty;
//
//import io.netty.handler.codec.http.HttpMethod;
//
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//import org.springframework.beans.BeansException;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//public class SpringContext {
//
//    //请求服务对象
//    public static final Map<String,InvokeObj> factory = new HashMap<String,InvokeObj>();
//
//    public void init(String spring,String _package) throws BeansException, ClassNotFoundException {
//
//        List<Class<?>> classes = Scan.getClasses(_package);
//
//        System.out.println("spring context is initing for netty... ");
//
//        @SuppressWarnings("resource")
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(spring);
//
//        for (Class<?> clz : classes) {
//
//            if(clz.isAnnotationPresent(Controller.class)) {
//                Object bean = context.getBean(clz);
//                String[] classUri = new String[0];
//
//                if(clz.isAnnotationPresent(RequestMapping.class)) {
//                    RequestMapping annotation = clz.getAnnotation(RequestMapping.class);
//                    String[] value = annotation.value();
//                    if(value != null && value.length > 0) {
//                        classUri = value;
//                    }
//                }
//
//                Method[] declaredMethods = clz.getDeclaredMethods();
//
//                for(Method method:declaredMethods) {
//
//                    if(method.isAnnotationPresent(RequestMapping.class)) {
//
//                        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
//
//                        RequestMethod[] supportMethod = annotation.method();
//
//                        if(supportMethod.length <= 0) {
//                            throw new RuntimeException("the method : "+method.getName()+" need to define a request method . for example GET/POST");
//                        }
//
//                        String[] methodUri = annotation.value();
//
//                        if(methodUri.length <= 0) {
//                            throw new RuntimeException("the method : "+method.getName()+" need to define a uri . for example /foo/bar");
//                        }
//
//                        for(String curi:classUri) {
//
//                            for(String muri:methodUri) {
//
//                                for(RequestMethod md:supportMethod) {
//                                    if(!md.name().equals(RequestMethod.GET.name()) && !md.name().equals(RequestMethod.POST.name())) {
//                                        throw new RuntimeException("the method : "+md.name()+" is not support . pls define a correct method. for example /foo/bar");
//                                    }
//
//                                    String requestUri = curi+muri;
//
//                                    InvokeObj invokeObj = new InvokeObj();
//                                    invokeObj.setMtd(method);
//                                    invokeObj.setObj(bean);
//                                    invokeObj.setUri(requestUri);
//                                    invokeObj.setHmtd(md.name());
//
//                                    String searchKey = md.name().toLowerCase()+"-"+requestUri;
//                                    if(factory.containsKey(searchKey)) {
//                                        throw new RuntimeException("the method : "+method.getName()+" 's uri is exits . pls define a other correct uri. for example /foo/bar");
//                                    }
//
//                                    factory.put(searchKey,invokeObj);
//
//                                    System.out.println(invokeObj);
//                                }
//
//
//                            }
//
//                        }
//
//
//                    }
//
//                }
//
//            }
//
//        }
//
//        System.out.println("spring context success to start for netty... ");
//    }
//
//    public static InvokeObj getBean(String uri,HttpMethod method) {
//        return factory.get(method.name().toLowerCase()+"-"+uri);
//    }
//
//    public static class InvokeObj {
//
//        /**
//         * 调用uri
//         */
//        private String uri;
//        /**
//         * 调用对象
//         */
//        private Object obj;
//        /**
//         * 调用方法
//         */
//        private Method mtd;
//
//        /**
//         * http method
//         */
//        private String hmtd;
//
//        public String getUri() {
//            return uri;
//        }
//        public void setUri(String uri) {
//            this.uri = uri;
//        }
//        public Object getObj() {
//            return obj;
//        }
//        public void setObj(Object obj) {
//            this.obj = obj;
//        }
//        public Method getMtd() {
//            return mtd;
//        }
//        public void setMtd(Method mtd) {
//            this.mtd = mtd;
//        }
//
//        public String getHmtd() {
//            return hmtd;
//        }
//        public void setHmtd(String hmtd) {
//            this.hmtd = hmtd;
//        }
//        @Override
//        public String toString() {
//            return "init access obj: <URI="+uri+",CLASS="+obj.getClass().getName()+",METHOD="+hmtd+">";
//        }
//    }
//}
