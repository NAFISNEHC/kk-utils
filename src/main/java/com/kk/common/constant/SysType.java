/**
 * Copyright (C), 2015-2019, 知融科技服务有限公司
 * FileName: SysType
 * Author:   allahbin
 * Date:     2019/9/20 18:24
 * Description: 系统类型的枚举
 */
package com.kk.common.constant;

/**
 * 〈一句话功能简述〉<br> 
 * 〈系统类型的枚举〉
 *
 * @author allahbin
 * @create 2019/9/20
 * @since 1.0.0
 */
public enum SysType {
    PC("PC"),
    WEB("WEB"),
    MOBILE("mobile");

    private String type;

    SysType(String type){
        this.type = type;
    }

    public boolean isPc(String types){
        return types.equals(PC.getType());
    }

    public boolean isWeb(String types){
        return types.equals(WEB.getType());
    }

    public boolean isMobile(String types){
        return types.equals(MOBILE.getType());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
