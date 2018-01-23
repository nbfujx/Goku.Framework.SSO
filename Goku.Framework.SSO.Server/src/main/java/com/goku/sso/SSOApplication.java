package com.goku.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Created by nbfujx on 2017/12/25.
 */
@SpringBootApplication
@ServletComponentScan
@MapperScan("com.goku.sso.**.mapper")
public class SSOApplication extends SpringBootServletInitializer {
    
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SSOApplication.class, args);
    }

}
