package com.goku.demo.config.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * Created by nbfujx on 2017/11/7.
 */
public class ShiroRealm extends AuthorizingRealm {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String userName=token.getUsername();
        //通过ssocode获取用户信息
        //SysUser user = sysuserextmapper.getUserBySSocode(token.getUsername());
        //模拟获取到的user
        HashMap<String,String> user = new HashMap<>();
        user.put("ssoCode",userName);
        user.put("name","nbfujx");
        user.put("sex","1");
        if (user != null) {
            //设置用户session
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute(token.getUsername(), user);
            return new SimpleAuthenticationInfo(userName,"ssotoken",getName());
        } else {
            return null;
        }
    }
}
