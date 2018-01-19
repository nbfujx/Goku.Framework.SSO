package com.goku.sso.controller;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;


/**
 *
 * @author nbfujx
 * @date 2018-01-16
 */

public interface LoginController {
    String login(HttpServletRequest request, HttpServletResponse response,
                 String token,String systemid);
    String logout(HttpServletRequest request, HttpServletResponse response,
                 String token,String systemid);
    String doLogin(HttpServletResponse response,
            Model model,String systemid, String userName, String password,boolean rememberMe);

}
