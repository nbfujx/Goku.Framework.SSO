package com.goku.sso.controller;

import com.goku.sso.model.SsoUser;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author nbfujx
 * @date 2018-01-18
 */
public interface SSOAuthController {

    /**
     * 用户注册/授权
     * **/
    String RegistryUser(String systemId,SsoUser ssoUser);
    /**
     * 用户修改
     * **/
    String ModifyUser(String systemId,SsoUser ssoUser);
    /**
     * 用户停用
     * **/
    String DisableUser(String systemId,String ssoCode);
    /**
     * 用户验证
     * **/
    String AuthUser(String token);
    /**
     * 用户登录
     * **/
    String login(HttpServletRequest request, HttpServletResponse response,
                 Model model,
                 String token, String systemid,
                 String url);
    /**
     * 用户登出
     * **/
    String logout(HttpServletRequest request, HttpServletResponse response,
                  Model model,
                  String token,String systemid);
    /**
     * 用户登录验证
     * **/
    String doLogin(HttpServletRequest request,HttpServletResponse response,
                   Model model,String url,String systemid, String userName, String password, boolean rememberMe);

}
