package com.goku.sso.model.ext;

/**
 * Created by nbfujx on 2018/1/19.
 */
public class SsoToken {
    private String ssoCode;
    private String email;
    private String idcard;
    private String mobile;

    public SsoToken(String ssoCode, String email, String idcard, String mobile) {
        this.ssoCode = ssoCode;
        this.email = email;
        this.idcard = idcard;
        this.mobile = mobile;
    }

    public String getSsoCode() {
        return ssoCode;
    }

    public void setSsoCode(String ssoCode) {
        this.ssoCode = ssoCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
