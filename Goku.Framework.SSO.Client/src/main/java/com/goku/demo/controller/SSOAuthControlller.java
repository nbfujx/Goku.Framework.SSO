package com.goku.demo.controller;

import com.goku.demo.model.SsoToken;
import com.goku.demo.service.SSOAuthService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by nbfujx on 2018/1/22.
 */
@Controller
public class SSOAuthControlller {

    @Value("${goku.sso.url}")
    private  String ssourl;

    @Value("${goku.sso.systemid}")
    private  String systemid;

    @Autowired
    SSOAuthService ssoAuthService;

    /****
     * 登录
     * */
    @RequestMapping("/login")
    public String login( @CookieValue(value="token",required=false) String token)
    {
        if(!"".equals(token)&&token!=null)
        {
            SsoToken ssotoken = ssoAuthService.AuthUser(token);
            if (ssotoken != null) {
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken shirotoken = new UsernamePasswordToken(ssotoken.getSsoCode(), "ssotoken");
                try {
                    subject.login(shirotoken);
                    return "redirect:/index";
                }catch (UnknownAccountException e) {
                }catch (IncorrectCredentialsException e) {
                }catch (AuthenticationException e) {
                }catch (Exception e) {
                }
            }else{
                return "redirect:"+ssourl+"login?systemid="+systemid;
            }

        }else
        {
            return "redirect:"+ssourl+"login?systemid="+systemid;
        }
        return null;
    }

    /****
     * 退出登录
     * */
    @RequestMapping("/logout")
    public String logout(HttpServletResponse response,
                         @CookieValue(value="token",required=false) String token)
    {
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.logout();
            return "redirect:"+ssourl+"logout?systemid="+systemid;
        }catch (SessionException e) {
        }catch (Exception e) {
        }
        return null;
    }

    /**
     * sso回调函数
     * **/
    @RequestMapping("/doAuth")
    public String doAuth(HttpServletResponse response,
                         @RequestParam(value = "token", required = true)  String token)
    {
        SsoToken ssotoken = ssoAuthService.AuthUser(token);
        if (ssotoken != null) {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken shirotoken = new UsernamePasswordToken(ssotoken.getSsoCode(), "ssotoken");
            try {
                subject.login(shirotoken);
                Cookie tokenck = new Cookie("token", token);
                response.addCookie(tokenck);
                return "redirect:/index";
            } catch (UnknownAccountException e) {
            } catch (IncorrectCredentialsException e) {
            } catch (AuthenticationException e) {
            } catch (Exception e) {
            }
        }
        return null;
    }

}
