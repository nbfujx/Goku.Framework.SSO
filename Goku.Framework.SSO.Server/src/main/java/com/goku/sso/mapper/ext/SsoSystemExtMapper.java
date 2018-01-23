package com.goku.sso.mapper.ext;

import com.goku.sso.mapper.SsoSystemMapper;
import com.goku.sso.model.SsoSystem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by nbfujx on 2018/1/19.
 */
public interface SsoSystemExtMapper extends SsoSystemMapper {
    SsoSystem selectByCode(@Param("code") String code);
    SsoSystem selectBySsoCode(@Param("code") String code, @Param("ssoCode") String ssoCode);
}
