package com.goku.demo.config.shiro;

import com.alibaba.fastjson.JSON;
import com.goku.demo.model.SsoToken;
import com.goku.demo.service.SSOAuthService;
import com.goku.demo.util.CookieUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nbfujx on 2018/1/14.
 */
public class SSOAuthShiroFilter extends AccessControlFilter {

    @Autowired
    CookieUtil cookieUtil;

    @Autowired
    SSOAuthService ssoAuthService;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        Subject subject = this.getSubject(httpRequest, httpResponse);
        if(subject.getPrincipal() == null) {
            saveRequest(httpRequest);
            WebUtils.issueRedirect(httpRequest, httpResponse, getLoginUrl());
            return false;
        }else {
            Cookie ck = cookieUtil.getCookieByName(httpRequest, "token");
            if (ck != null) {
                String token = ck.getValue();
                SsoToken ssotoken = ssoAuthService.AuthUser(token);
                if (ssotoken != null) {
                    return true;
                } else {
                    if (isAjax(httpRequest)) {
                        try {
                            SecurityUtils.getSubject().logout();
                        } catch (SessionException e) {
                        } catch (Exception e) {
                        } finally {
                            Map<String, String> resultMap = new HashMap<>();
                            resultMap.put("status", "401");
                            Cookie tokenck = cookieUtil.delCookie(httpRequest, "token");
                            httpResponse.addCookie(tokenck);
                            out(httpResponse, resultMap);
                            return false;
                        }
                    } else {
                        try {
                            SecurityUtils.getSubject().logout();
                        } catch (SessionException e) {
                        } catch (Exception e) {
                        } finally {
                            Cookie tokenck = cookieUtil.delCookie(httpRequest, "token");
                            httpResponse.addCookie(tokenck);
                            saveRequest(httpRequest);
                            WebUtils.issueRedirect(httpRequest, httpResponse,getLoginUrl());
                            return false;
                        }
                    }
                }
            } else {
                if (isAjax(httpRequest)) {
                    try {
                        SecurityUtils.getSubject().logout();
                    } catch (SessionException e) {
                    } catch (Exception e) {
                    } finally {
                        Map<String, String> resultMap = new HashMap<>();
                        resultMap.put("status", "401");
                        out(httpResponse, resultMap);
                        return false;
                    }
                } else {
                    try {
                        SecurityUtils.getSubject().logout();
                    } catch (SessionException e) {
                    } catch (Exception e) {
                    } finally {
                        saveRequest(httpRequest);
                        WebUtils.issueRedirect(httpRequest, httpResponse, getLoginUrl());
                        return false;
                    }
                }
            }
        }
    }


    /**
     * 是否是Ajax请求
     * @param request
     * @return
     */
    public static boolean isAjax(ServletRequest request){
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if("XMLHttpRequest".equalsIgnoreCase(header)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     *  使用	response 输出JSON
     * @param response
     * @param resultMap
     * @throws IOException
     */
    public static void out(ServletResponse response, Map<String, String> resultMap){
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");//设置编码
            response.setContentType("application/json");//设置返回类型
            out = response.getWriter();
            out.println(JSON.toJSON(resultMap));//输出
        } catch (Exception e) {

        }finally{
            if(null != out){
                out.flush();
                out.close();
            }
        }
    }

}
