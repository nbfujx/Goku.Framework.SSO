package com.goku.sso.controller.impl;

import com.goku.sso.controller.LoginController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


/**
 *
 * @author nbfujx
 * @date 2018-01-16
 */
@Controller
public class LoginControllerImpl  implements LoginController {


    @RequestMapping("/login")
    public String login(HttpServletResponse response,
                        @RequestParam(value = "systemid") String systemid) {
        String returnURL="";
        return returnURL;
    }

    @RequestMapping("/doLogin")
    public String doLogin(HttpServletResponse response,
                           RedirectAttributes attr,
                           @CookieValue(value="redirectUrl",required=false) String redirectUrl,
                           @CookieValue(value="systemUrl",required=false) String systemUrl,
                           @RequestParam(value = "username", required = true) String userName,
                           @RequestParam(value = "password", required = true)  String password) {
        String uuid = UUID.randomUUID().toString();
        Cookie uuidcookie = new Cookie("JSESSIONID",uuid);
        response.addCookie(uuidcookie);
        return "redirect:"+systemUrl+"/doAuth?JSESSIONID="+ uuid+"&url="+redirectUrl;
    }
}
