package com.doopp.gauss.server.task;

import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

public class WolfKillGame {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    private class GameTask implements Runnable {

        public void run()  {

            // logger.info(" >>> Run GameTask " + userService);
            while(true) {
                if (userService==null) {
                    continue;
                }
                UserEntity user = userService.getUserInfo("koocyton@gmail.com");
                logger.info(" >>> Run GameTask " + user);
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private TaskExecutor taskExecutor;

    public WolfKillGame(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
        this.launch();
    }

    private void launch() {
        taskExecutor.execute(new GameTask());
    }
}
