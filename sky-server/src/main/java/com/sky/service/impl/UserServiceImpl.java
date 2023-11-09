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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    public User wxLogin(UserLoginDTO userLoginDTO) {
        //使用HttpClient发送get请求到微信接口服务,返回openid
        String code = userLoginDTO.getCode();
        String openid = getOpenid(code);
        //判断openid是否为空,为空则抛出异常
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //用openid查询User表,为空则自动保存
        User user = userMapper.getByOpenid(openid);

        if (user == null) {
            user=User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        //返回User对象
        return user;
    }


    /**
     * 获取微信用户的openid
     *
     * @param code
     * @return
     */
    private String getOpenid(String code) {
        Map map = new HashMap();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");

        String json = HttpClientUtil.doGet(WX_LOGIN, map);
        log.info("微信登录返回结果：{}", json);

        //解析字符串
        JSONObject object = JSON.parseObject(json);
        String openid = object.getString("openid");
        log.info("微信用户的openid为：{}", openid);

        return openid;
    }
}