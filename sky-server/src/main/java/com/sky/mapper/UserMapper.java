package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 根据openid查询用户
     *
     * @param openId openid
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenId(String openId);

    /**
     * 插入数据
     *
     * @param user 用户数据
     */
    void insert(User user);

}
