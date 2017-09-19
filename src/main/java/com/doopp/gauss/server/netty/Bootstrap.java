//package com.doopp.gauss.server.netty;
//
//import java.util.Properties;
//
//public class Bootstrap {
//
//    /**
//     * Daemon object used by main.
//     */
//    private static Server daemon = null;
//
//    /**
//     * @param args
//     */
//    private static void comeon(String port,String _package,String spring) throws Exception {
//
//        if (daemon == null) {
//            daemon = new HttpServer(Integer.parseInt(port));
//            daemon.init(spring,_package);
//        }
//
//        daemon.start();
//    }
//
//    public static void start() throws Exception {
//
//        Properties prop = new Properties();
//        prop.load(Bootstrap.class.getResourceAsStream("/setting.properties"));
//
//        String port = prop.get("startup.port").toString();
//        String spring = prop.get("startup.spring.file").toString();
//        String pkg = prop.get("startup.package").toString();
//
//        comeon(port,pkg,spring);
//
//    }
//
//}
