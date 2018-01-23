package com.goku.demo.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nbfujx on 2018/1/19.
 */
@Component
public class CookieUtil {

    public Cookie getCookieByName(HttpServletRequest request, String name){
        Map<String,Cookie> cookieMap = ReadCookieMap(request);
        if(cookieMap.containsKey(name)){
            Cookie cookie = (Cookie)cookieMap.get(name);
            return cookie;
        }else{
            return null;
        }
    }

    public  Map<String,Cookie> ReadCookieMap(HttpServletRequest request){
        Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
        Cookie[] cookies = request.getCookies();
        if(null!=cookies){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

    public Cookie addCookie(String name,String value){
        Cookie cookie = new Cookie(name.trim(), value.trim());
        cookie.setMaxAge(30 * 60);// 设置为30min
        cookie.setPath("/");
        return cookie;
    }

    public Cookie editCookie(HttpServletRequest request,String name,String value){
        Cookie cookie = getCookieByName(request,name);
        if(cookie!=null) {
            cookie.setValue(value);
            cookie.setPath("/");
            cookie.setMaxAge(30 * 60);
        }else
        {
            cookie= new Cookie(name.trim(), value.trim());
            cookie.setMaxAge(30 * 60);// 设置为30min
            cookie.setPath("/");
        }
        return cookie;
    }

    public Cookie delCookie(HttpServletRequest request,String name){
        Cookie cookie = getCookieByName(request,name);
        cookie.setValue(null);
        cookie.setMaxAge(0);// 立即销毁cookie
        cookie.setPath("/");
        return cookie;
    }
}
