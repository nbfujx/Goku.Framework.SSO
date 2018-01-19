package com.goku.sso.mapper.ext;

import com.goku.sso.mapper.SsoUserMapper;
import com.goku.sso.model.SsoUser;

/**
 * Created by nbfujx on 2018-01-19.
 */
public interface SsoUserExtMapper extends SsoUserMapper {
    SsoUser selectByUsername(String username);
    SsoUser selectBySsoCode(String sso_code);
}
