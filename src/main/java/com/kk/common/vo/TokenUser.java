/**
 * Copyright (C), 2015-2019, 知融科技服务有限公司
 * FileName: UserInfo
 * Author:   allahbin
 * Date:     2019/9/20 18:04
 * Description: 用户信息
 */
package com.kk.common.vo;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户信息〉
 *
 * @author allahbin
 * @create 2019/9/20
 * @since 1.0.0
 */
public class TokenUser {
    /**
     * 用户id
     */
    private String userId;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 部门编码
     */
    private String depCode;
    /**
     * 部门名称
     */
    private String depName;
    /**
     * 部门id
     */
    private String depId;
    /**
     * 用户令牌
     */
    private String token;
    /**
     * 服务id
     */
    private String deviceId;
    /**
     * 系统类型
     */
    private String sysType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSysType() {
        return sysType;
    }

    public void setSysType(String sysType) {
        this.sysType = sysType;
    }
}
