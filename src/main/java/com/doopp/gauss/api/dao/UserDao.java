package com.doopp.gauss.api.dao;

import com.doopp.gauss.api.entity.UserEntity;
// import com.doopp.gauss.api.helper.DBSession;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/*
 * Created by henry on 2017/7/4.
 */
public interface UserDao {

    void create(UserEntity userEntity);

    void update(UserEntity userEntity);

    void delete(int id);

    Long count(String where);

    Long count();

    UserEntity fetchById(long id);

    List<UserEntity> fetchUserFriends(Long id);

    List<UserEntity> fetchListByIds(@Param("ids") String ids, @Param("offset") int offset, @Param("limit") int limit);

    UserEntity fetchByAccount(String account);

    List<UserEntity> fetchList(@Param("offset") int offset, @Param("limit") int limit);

    List<UserEntity> fetchList(@Param("where") String where, @Param("offset") int offset, @Param("limit") int limit);
}
