package com.goku.sso.controller.impl;

import com.alibaba.fastjson.JSON;
import com.goku.sso.controller.AuthController;
import com.goku.sso.model.ext.SsoToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nbfujx
 * @date 2018-01-18
 */
@RestController
public class AuthControllerImpl implements AuthController {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public String RegistryUser(String systemId, SsoUser ssoUser) {
        return null;
    }

    @Override
    public String ModifyUser(String systemId, SsoUser ssoUser) {
        return null;
    }

    @Override
    public String DisableUser(String systemId, String ssoCode) {
        return null;
    }

    @Override
    @RequestMapping("/AuthUser/{token}")
    public String AuthUser(@PathVariable String token) {
        SsoToken ssoToken= (SsoToken) redisTemplate.opsForValue().get(token);
        return JSON.toJSONString(ssoToken);
    }
}
