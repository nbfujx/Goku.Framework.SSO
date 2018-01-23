package com.goku.sso.controller.impl;

import com.alibaba.fastjson.JSON;
import com.goku.sso.controller.SSOAuthController;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 *
 * @author nbfujx
 * @date 2018-01-18
 */
@Controller
@RequestMapping("/service")
public class SSOAuthControllerImpl implements SSOAuthController {

    @Autowired
    SsoSystemService ssoSystemService;

    @Autowired
    CookieUtil cookieUtil;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RestTemplate restTemplate;

    @Override
    @ResponseBody
    public String RegistryUser(String systemId, SsoUser ssoUser) {
        return null;
    }

    @Override
    @ResponseBody
    public String ModifyUser(String systemId, SsoUser ssoUser) {
        return null;
    }

    @Override
    @ResponseBody
    public String DisableUser(String systemId, String ssoCode) {
        return null;
    }

    @Override
    @RequestMapping("/authUser")
    @ResponseBody
    public String AuthUser(@RequestParam(value = "token", required = true)  String token) {
        SsoToken ssoToken= (SsoToken) redisTemplate.opsForValue().get(token);
        return JSON.toJSONString(ssoToken);
    }

    @Override
    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response,
                        Model model,
                        @CookieValue(value="token",required=false) String token,
                        @RequestParam(value = "systemid", required = true)  String systemid,
                        @RequestParam(value = "url", required = false,defaultValue = "")  String url) {
        SsoSystem system=ssoSystemService.selectByCode(systemid);
        String returnURL="";
        if(system==null){
            returnURL="nofound";
        }else{
            if("".equals(token)||token==null) {
                model.addAttribute("systemid",systemid);
                model.addAttribute("url",url);
                returnURL =system.getLoginUrl();
            }else
            {
                SsoToken ssoToken= (SsoToken) redisTemplate.opsForValue().get(token);
                if(ssoToken!=null) {
                    url=url==""?system.getIndexUrl():url;
                    returnURL = "redirect:" + system.getUrl() + "/doAuth?token=" + token + "&url=" + url;
                }else{
                    Cookie tokenck = cookieUtil.delCookie(request,"token");
                    response.addCookie(tokenck);
                    model.addAttribute("systemid",systemid);
                    model.addAttribute("url",url);
                    returnURL =system.getLoginUrl();
                }
            }
        }

        return returnURL;
    }

    @Override
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,
                         Model model,
                         @CookieValue(value="token",required=false) String token,
                         @RequestParam(value = "systemid", required = true) String systemid) {
        String returnURL="";
        SsoSystem system=ssoSystemService.selectByCode(systemid);
        if(system==null){
            returnURL="nofound";
        }else{
            if("".equals(token)||token==null) {
                Cookie systemck = cookieUtil.editCookie(request,"systemid",systemid);
                response.addCookie(systemck);
                model.addAttribute("systemid",systemid);
                returnURL =system.getLoginUrl();
            }else
            {
                Subject currentUser = SecurityUtils.getSubject();
                try {
                    Cookie tokenck = cookieUtil.delCookie(request,"token");
                    Cookie systemck = cookieUtil.editCookie(request,"systemid",systemid);
                    response.addCookie(tokenck);
                    response.addCookie(systemck);
                    redisTemplate.delete(currentUser.getPrincipal());
                    redisTemplate.delete(token);
                    currentUser.logout();
                    model.addAttribute("systemid",systemid);
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
    public String doLogin(HttpServletRequest request,HttpServletResponse response,
                          Model model,
                          @RequestParam(value="systemid",required=false) String systemid,
                          @RequestParam(value="url",required=false) String url,
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
                SsoUser user = (SsoUser) subject.getSession().getAttribute(subject.getPrincipal());
                SsoToken ssoToken=new SsoToken(user.getSsoCode(),user.getEmail(),user.getIdcard(),user.getMobile());
                //删除原来存在的用户信息
                String strtoken= (String) redisTemplate.opsForValue().get(user.getSsoCode());
                if(strtoken!=null) {
                    redisTemplate.delete(user.getSsoCode());
                    redisTemplate.delete(strtoken);
                }
                //登记新的用户信息，强制下线原有的用户
                redisTemplate.opsForValue().set(user.getSsoCode(),uuid);
                redisTemplate.opsForValue().set(uuid,ssoToken);
                url=url==""?system.getIndexUrl():url;
                returnURL="redirect:"+system.getUrl()+"/doAuth?token="+ uuid+"&url="+url;
            } catch (UnknownAccountException e) {
                model.addAttribute("systemid",systemid);
                model.addAttribute("error", "账号不存在!");
                returnURL=system.getLoginUrl();
            } catch (IncorrectCredentialsException e) {
                model.addAttribute("systemid",systemid);
                model.addAttribute("error", "账号密码错误!");
                returnURL=system.getLoginUrl();
            } catch (AuthenticationException e) {
                model.addAttribute("systemid",systemid);
                model.addAttribute("error", "登录异常!请联系管理员!");
                returnURL=system.getLoginUrl();
            } catch (Exception e) {
                model.addAttribute("systemid",systemid);
                model.addAttribute("error", "系统异常!");
                returnURL=system.getLoginUrl();
            }
        }
        return returnURL;
    }

}
