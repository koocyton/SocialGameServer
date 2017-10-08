package com.doopp.gauss.server.undertow;

import com.doopp.gauss.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

public class GuessDrawGame implements DisposableBean, Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Thread thread;

    private volatile boolean runCondition = true;

    @Autowired
    UserService userService;

    GuessDrawGame(){
        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        logger.info(" >>> DEMO : run com.doopp.gauss.server.undertow.GuessDrawGame");
        /* while(runCondition) {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                System.out.print(" >>> InterruptedException " + e.getMessage());
                break;
            }
            System.out.print(" >>> " + userService + "\n     " + this.thread.getContextClassLoader() + "\n");
        } */
    }

    @Override
    public void destroy(){
        runCondition = false;
    }
}
