package com.aitongyi.web.back.controller;

import com.aitongyi.web.bean.User;
import com.aitongyi.web.cache.CacheService;
import com.aitongyi.web.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用户请求处理
 * Created by Administrator on 2017/1/19.
 */
@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")// isAuthenticated 如果用户不是匿名用户就返回true
    public String showHomePage() {
        try {
            User user = userService.loadUserByUsername("admin");

//          测试缓存服务
//          缓存用户对象到redis,以用户ID区分
            cacheService.set("LOGIN_USER_" + user.getId(), JSON.toJSONString(user));
//          从缓存中取出
            String userStr = cacheService.get("LOGIN_USER_" + user.getId());
//          进行反序列化
            User u = JSON.parseObject(userStr, User.class);
            if(u != null){
                logger.info("user:{}", u);
            }
            logger.info("load user ");
        }catch (Exception e){
            logger.error(e.getLocalizedMessage(), e);
        }

        return "/index/index";
    }
}
