package com.we.mvp.user.login.modle;

/**
 * Created by 74234 on 2017/5/8.
 */

public class WeiboRrfreshToken {
    private String access_token;
    private Long expires_in;

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {

        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
