package com.goku.sso.service;

import com.goku.sso.model.SsoSystem;

import java.util.List;

/**
 * Created by nbfujx on 2018/1/19.
 */
public interface SsoSystemService {
    SsoSystem selectByCode(String code);
    List<SsoSystem> selectBySsoCode(String SsoCode);
}
