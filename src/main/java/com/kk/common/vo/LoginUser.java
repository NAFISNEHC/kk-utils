package com.kk.common.vo;

public class LoginUser {

    /**
     * 用户id
     */
    private String userid;
    /**
     * 登录名
     */
    private String loginname;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 用户类型
     */
    private String usertype;
    /**
     * 部门编码
     */
    private String depcode;
    /**
     * 部门名称
     */
    private String depname;
    /**
     * 用户令牌
     */
    private String token;
    /**
     * 服务id
     */
    private String deviceid;
    /**
     * 系统类型
     */
    private String sysType;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getDepcode() {
        return depcode;
    }

    public void setDepcode(String depcode) {
        this.depcode = depcode;
    }

    public String getDepname() {
        return depname;
    }

    public void setDepname(String depname) {
        this.depname = depname;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }
}
