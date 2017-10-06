package com.doopp.gauss.server.undertow;

import com.doopp.gauss.api.service.UserService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

public class GuessDrawGame implements DisposableBean, Runnable {

    private Thread thread;

    private volatile boolean runCondition = true;

    @Autowired
    UserService userService;

    GuessDrawGame(){
        this.thread = new Thread(this);
    }

    @Override
    public void run() {
        System.out.print(" >>> DEMO : run com.doopp.gauss.server.undertow.GuessDrawGame \n ");
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
