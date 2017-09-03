package com.doopp.gauss.api.helper;

import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.impl.LoginServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisSessionHelper {


    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("config/redis/spring-jedis.xml");

    private final RedisTemplate redisTemplate =  (RedisTemplate) applicationContext.getBean("redisSessionTemplate");

    private final ValueOperations<String, Object> ofv = redisTemplate.opsForValue();

    /*
     * Properties properties = new Properties();
     * FileInputStream fileInputStream = new FileInputStream("/data/app/conf/tomcat.webapp.root.properties")
     * InputStream inputStream = new BufferedInputStream(fileInputStream);
     * properties.load(inputStream);
     * String staticHost = properties.getProperty("static.host");
     */

    public UserEntity getUserByToken(String accessToken) {
        Object userObject = ofv.get(accessToken);
        logger.info(" >>> userObject " + userObject);
        if (userObject!=null) {
            return (UserEntity) userObject;
        }
        return null;
    }

    public void setUserByToken(String accessToken, UserEntity userEntity) {
        ofv.set(accessToken, userEntity);
    }
}
