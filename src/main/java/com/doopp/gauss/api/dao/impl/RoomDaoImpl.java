package com.doopp.gauss.api.dao.impl;

import com.doopp.gauss.api.dao.RoomDao;
import com.doopp.gauss.api.entity.RoomEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/*
 * 操作 RoomEntity 的 Dao
 */
@Service
public class RoomDaoImpl implements RoomDao {

    // private RedisTemplate redisTemplate;

    @Resource
    private ShardedJedisPool shardedJedisPool;

    private static final Logger logger = LoggerFactory.getLogger(RoomDaoImpl.class);

    @Override
    public void create(RoomEntity roomEntity) {
        logger.info(" >>> shardedJedisPool " + shardedJedisPool);
        // ValueOperations<Serializable, Serializable> opsForValue = redisTemplate.opsForValue();
        //logger.info(" >>> redisTemplate " + redisTemplate);
        //logger.info(" >>> redisTemplate " + redisTemplate.opsForValue());
        // User user2 = (User) opsForValue.get(user.getId());
        // return user2;
    }

    @Override
    public void delete(int roomId) {

    }

    @Override
    public void update(RoomEntity roomEntity) {

    }

    @Override
    public Long count() {
        return null;
    }

    @Override
    public RoomEntity fetchById(long id) {
        return null;
    }
}
