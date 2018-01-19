package com.goku.sso.controller.impl;

import com.goku.sso.controller.LoginController;
import com.goku.sso.model.SsoSystem;
import com.goku.sso.model.SsoUser;
import com.goku.sso.model.ext.SsoToken;
import com.goku.sso.service.SsoSystemService;
import com.goku.sso.util.CookieUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


/**
 *
 * @author nbfujx
 * @date 2018-01-16
 */
@Controller
public class LoginControllerImpl  implements LoginController {


    @Autowired
    SsoSystemService ssoSystemService;

    @Autowired
    CookieUtil cookieUtil;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    @RequestMapping("/login/{systemid}")
    public String login(HttpServletRequest request,HttpServletResponse response,
                        @CookieValue(value="token",required=false) String token,
                        @PathVariable String systemid) {
        SsoSystem system=ssoSystemService.selectByCode(systemid);
        String returnURL="";
        if(system==null){
            returnURL="nofound";
        }else{
            if("".equals(token)||token==null) {
                Cookie systemck = cookieUtil.editCookie(request,"systemid",systemid);
                response.addCookie(systemck);
                returnURL =system.getLoginUrl();
            }else
            {
                returnURL = "redirect:"+system.getUrl()+"/doAuth?token="+ token+"&url="+system.getIndexUrl();
            }
        }

        return returnURL;
    }

    @Override
    @RequestMapping("/logout/{systemid}")
    public String logout(HttpServletRequest request, HttpServletResponse response,
                         @CookieValue(value="token",required=false) String token,
                         @PathVariable String systemid) {
        String returnURL="";
        SsoSystem system=ssoSystemService.selectByCode(systemid);
        if(system==null){
            returnURL="nofound";
        }else{
            if("".equals(token)||token==null) {
                Cookie systemck = cookieUtil.editCookie(request,"systemid",systemid);
                response.addCookie(systemck);
                returnURL =system.getLoginUrl();
            }else
            {
                Subject currentUser = SecurityUtils.getSubject();
                try {

                    currentUser.logout();
                    Cookie tokenck = cookieUtil.delCookie(request,"token");
                    Cookie systemck = cookieUtil.editCookie(request,"systemid",systemid);
                    response.addCookie(tokenck);
                    response.addCookie(systemck);
                    redisTemplate.delete(token);
                    returnURL = system.getLoginUrl();
                } catch (SessionException ise) {
                    returnURL="error";
                } catch (Exception e) {
                    returnURL="error";
                }
            }
        }
        return returnURL;
    }

    @Override
    @RequestMapping("/doLogin")
    public String doLogin(HttpServletResponse response,
                          Model model,
                          @CookieValue(value="systemid",required=false) String systemid,
                          @RequestParam(value = "username", required = true) String userName,
                          @RequestParam(value = "password", required = true)  String password,
                          @RequestParam(value = "rememberMe", required = false, defaultValue = "false") boolean rememberMe) {
        String returnURL="";
        SsoSystem system=ssoSystemService.selectByCode(systemid);
        if(system==null){
            returnURL="nofound";
        }else {
            String passwordmd5 = new Md5Hash(password, "2").toString();
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(userName, passwordmd5);
            token.setRememberMe(rememberMe);
            try {
                subject.login(token);
                String uuid = UUID.randomUUID().toString();
                Cookie tokenck = cookieUtil.addCookie("token", uuid);
                response.addCookie(tokenck);
                SsoUser user = (SsoUser) subject.getSession().getAttribute("user");
                redisTemplate.opsForValue().set(uuid,new SsoToken(user.getSsoCode(),user.getEmail(),user.getIdcard(),user.getMobile()));
                returnURL="redirect:"+system.getUrl()+"/doAuth?token="+ token+"&url="+system.getIndexUrl();
            } catch (UnknownAccountException e) {
                model.addAttribute("error", "账号不存在!");
                returnURL=system.getLoginUrl();
            } catch (IncorrectCredentialsException e) {
                model.addAttribute("error", "账号密码错误!");
                returnURL=system.getLoginUrl();
            } catch (AuthenticationException e) {
                model.addAttribute("error", "登录异常!请联系管理员!");
                returnURL=system.getLoginUrl();
            } catch (Exception e) {
                model.addAttribute("error", "系统异常!");
                returnURL=system.getLoginUrl();
            }
        }
        return returnURL;
    }
}
