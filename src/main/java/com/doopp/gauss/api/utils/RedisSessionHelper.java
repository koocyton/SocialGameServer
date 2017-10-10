package com.doopp.gauss.api.utils;

import com.doopp.gauss.api.entity.UserEntity;
import com.doopp.gauss.api.service.impl.LoginServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.concurrent.TimeUnit;

@Component
public class RedisSessionHelper {


    // private final Logger logger = LoggerFactory.getLogger(getClass());

    private static ApplicationContext applicationContext = new ClassPathXmlApplicationContext("config/redis/spring-jedis.xml");

    private final RedisTemplate redisTemplate =  (RedisTemplate) applicationContext.getBean("redisSessionTemplate");

    // private final ShardedJedisPool sessionJedisPool =  (ShardedJedisPool) applicationContext.getBean("sessionJedis", ShardedJedisPool.class);

    // private final ShardedJedis sessionJedis = sessionJedisPool.getResource();

    private final ValueOperations<String, Object> ofv = redisTemplate.opsForValue();

    // private final JdkSerializationRedisSerializer jsrs = new JdkSerializationRedisSerializer();

    /*
     * Properties properties = new Properties();
     * FileInputStream fileInputStream = new FileInputStream("/data/app/conf/tomcat.webapp.root.properties")
     * InputStream inputStream = new BufferedInputStream(fileInputStream);
     * properties.load(inputStream);
     * String staticHost = properties.getProperty("public.host");
     */

    public void delUserSessionCache(String accessToken) {
        ofv.getOperations().delete(accessToken);
    }

    public UserEntity getUserByToken(String accessToken) {
        // 取值
        Object userObject = ofv.get(accessToken);
        // 如果取出数据为空
        if (userObject==null) {
            return null;
        }
        // 返回用户信息
        return (UserEntity) userObject;
    }

    public void setUserByToken(String accessToken, UserEntity userEntity) {
        // 先查询有没有旧数据
        Long userId = userEntity.getId();
        Object lastAccesssToken = ofv.get(String.valueOf(userId));

        // 删除旧的索引和旧的数据
        // 使得之前的 access token 失效
        if (lastAccesssToken!=null) {
            ofv.getOperations().delete(String.valueOf(userId));
            ofv.getOperations().delete((String) lastAccesssToken);
        }
        // 保存数据
        ofv.set(accessToken, userEntity, 7, TimeUnit.DAYS);
        // 保存 user id => access token 的索引
        ofv.set(String.valueOf(userId), accessToken);
    }

    /*
    public UserEntity getUserByToken(String accessToken) {
        // 拿到 redis 资源
        ShardedJedis sessionJedis = sessionJedisPool.getResource();

        // 转化 key
        byte[] byteAccessToken = accessToken.getBytes();
        // 从 key 里拿数据
        byte[] byteUserEntity = sessionJedis.get(byteAccessToken);

        // 如果取出数据为空
        if (byteUserEntity==null) {
            return null;
        }

        // 反序列化
        Object userObject = jsrs.deserialize(byteUserEntity);
        // 反成功
        if (userObject!=null) {
            return (UserEntity) userObject;
        }
        sessionJedis.close();
        return null;
    }

    public void setUserByToken(String accessToken, UserEntity userEntity) {
        // 拿到 redis 资源
        ShardedJedis sessionJedis = sessionJedisPool.getResource();

        // 先查询有没有旧数据
        Long userId = userEntity.getId();
        String lastAccesssToken = sessionJedis.get(String.valueOf(userId));

        // 删除旧的索引和旧的数据
        // 使得之前的 access token 失效
        if (lastAccesssToken!=null) {
            sessionJedis.del(String.valueOf(userId));
            sessionJedis.del(lastAccesssToken.getBytes());
        }

        // 将 UserEntity 序列化后保存
        byte[] accessTokenByte = accessToken.getBytes();
        byte[] userEntityByte = jsrs.serialize(userEntity);
        // 保存数据
        sessionJedis.set(accessTokenByte, userEntityByte);
        // 保存 user id => access token 的索引
        sessionJedis.set(String.valueOf(userId), accessToken);
        sessionJedis.close();
    }
    */
}
