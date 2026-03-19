package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {


    private final UserMapper userMapper;
    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";


    private final WeChatProperties weChatProperties;

    public UserServiceImpl(UserMapper userMapper, WeChatProperties weChatProperties) {
        this.userMapper = userMapper;
        this.weChatProperties = weChatProperties;
    }

    /**
     * 微信登录
     *
     * @param dto 登录参数
     * @return 用户信息
     */
    @Override
    public User wxLogin(UserLoginDTO dto) {

        // 1. 调用微信接口获取当前用户的openId
        String openId = getOpenId(dto.getCode());

        // 2. 判断openId是否为空，如果为空则表示失败
        if (openId == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 3. 判断当前用户是否是新用户
        // 3.1 根据openId查询数据库
        User user = userMapper.getByOpenId(openId);

        // 4. 如果是新用户，则插入数据
        if (user == null) {
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        // 5. 返回

        return user;
    }


    private String getOpenId(String code) {
        // 调用微信接口，获取openId
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        String json = HttpClientUtil.doGet(WX_LOGIN_URL, map);

        JSONObject jsonObject = JSON.parseObject(json);

        return jsonObject.getString("openid");
    }

}
