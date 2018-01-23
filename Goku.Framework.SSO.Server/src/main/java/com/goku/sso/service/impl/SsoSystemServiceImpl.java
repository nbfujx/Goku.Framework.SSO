package com.goku.sso.service.impl;

import com.goku.sso.mapper.ext.SsoSystemExtMapper;
import com.goku.sso.model.SsoSystem;
import com.goku.sso.service.SsoSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nbfujx on 2018/1/19.
 */
@Service
public class SsoSystemServiceImpl implements SsoSystemService {

    @Autowired
    SsoSystemExtMapper ssoSystemExtMapper;

    @Override
    public SsoSystem selectByCode(String code) {
        return ssoSystemExtMapper.selectByCode(code);
    }

    @Override
    public SsoSystem selectBySsoCode(String code,String SsoCode) {
        return ssoSystemExtMapper.selectBySsoCode(code,SsoCode);
    }
}
