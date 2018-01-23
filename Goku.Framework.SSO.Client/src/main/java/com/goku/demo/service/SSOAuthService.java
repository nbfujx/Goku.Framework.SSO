package com.goku.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.goku.demo.model.SsoToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by nbfujx on 2018/1/22.
 */
@Service
public class SSOAuthService{

    @Value("${goku.sso.url}")
    private  String ssourl;

    @Autowired
    private RestTemplate restTemplate;

    public SsoToken AuthUser(String token)
    {
        String url=ssourl + "authUser?token="+token;
        String u = this.restTemplate.getForObject(url, String.class);
        SsoToken ssoToken= JSON.parseObject(u, new TypeReference<SsoToken>() {});
        return ssoToken;
    }


}
