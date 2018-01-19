package com.goku.sso.service.impl;

import com.goku.sso.mapper.ext.SsoSystemExtMapper;
import com.goku.sso.model.SsoSystem;
import com.goku.sso.service.SsoSystemService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by nbfujx on 2018/1/19.
 */
public class SsoSystemServiceImpl implements SsoSystemService {

    @Autowired
    SsoSystemExtMapper ssoSystemExtMapper;

    @Override
    public SsoSystem selectByCode(String code) {
        return ssoSystemExtMapper.selectByCode(code);
    }
}
