package com.kk.common.vo; /**
 * Copyright (C), 2015-2019, 知融科技服务有限公司
 * FileName: PageData
 * Author:   allahbin
 * Date:     2019/8/6 10:41
 * Description: 分页数据
 */

/**
 * 〈一句话功能简述〉<br> 
 * 〈分页数据〉
 *
 * @author allahbin
 * @create 2019/8/6
 * @since 1.0.0
 */
public class HttpBody<T> {
    private String code;
    private String msg;

    private T result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
