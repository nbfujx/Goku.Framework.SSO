package com.goku.sso.controller;

/**
 *
 * @author nbfujx
 * @date 2018-01-18
 */
public interface AuthController {

    /**
     * 用户注册
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

}
