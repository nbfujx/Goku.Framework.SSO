package com.goku.sso.config.listener;

import com.goku.sso.model.SsoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.websocket.Session;

/**
 *
 * @author nbfujx
 * @date 2018-01-18
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
       //key:token valve:session
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        SsoUser user = (SsoUser) httpSessionEvent.getSession().getAttribute("user");
        String strtoken= (String) redisTemplate.opsForValue().get(user.getSsoCode());
        if(strtoken!=null) {
            redisTemplate.delete(user.getSsoCode());
            redisTemplate.delete(strtoken);
        }
    }
}
