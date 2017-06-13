package com.we.mvp.user.login.modle;

public class QQLoginInfo
{
    private String access_token;
    private int authority_cost;
    private String expires_in;
    private int login_cost;
    private String msg;
    private String openid;
    private String pay_token;
    private String pf;
    private String pfkey;
    private int query_authority_cost;
    private int ret;

    public String getAccess_token()
    {
        return this.access_token;
    }

    public int getAuthority_cost()
    {
        return this.authority_cost;
    }

    public String getExpires_in()
    {
        return this.expires_in;
    }

    public int getLogin_cost()
    {
        return this.login_cost;
    }

    public String getMsg()
    {
        return this.msg;
    }

    public String getOpenid()
    {
        return this.openid;
    }

    public String getPay_token()
    {
        return this.pay_token;
    }

    public String getPf()
    {
        return this.pf;
    }

    public String getPfkey()
    {
        return this.pfkey;
    }

    public int getQuery_authority_cost()
    {
        return this.query_authority_cost;
    }

    public int getRet()
    {
        return this.ret;
    }

    public void setAccess_token(String paramString)
    {
        this.access_token = paramString;
    }

    public void setAuthority_cost(int paramInt)
    {
        this.authority_cost = paramInt;
    }

    public void setExpires_in(String paramString)
    {
        this.expires_in = paramString;
    }

    public void setLogin_cost(int paramInt)
    {
        this.login_cost = paramInt;
    }

    public void setMsg(String paramString)
    {
        this.msg = paramString;
    }

    public void setOpenid(String paramString)
    {
        this.openid = paramString;
    }

    public void setPay_token(String paramString)
    {
        this.pay_token = paramString;
    }

    public void setPf(String paramString)
    {
        this.pf = paramString;
    }

    public void setPfkey(String paramString)
    {
        this.pfkey = paramString;
    }

    public void setQuery_authority_cost(int paramInt)
    {
        this.query_authority_cost = paramInt;
    }

    public void setRet(int paramInt)
    {
        this.ret = paramInt;
    }
}
