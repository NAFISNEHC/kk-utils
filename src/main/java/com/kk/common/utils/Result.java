/**
 * Copyright (C), 2015-2019, 知融科技服务有限公司
 * FileName: Result
 * Author:   allahbin
 * Date:     2018/7/25 15:13
 * Description: 用于返回数据的方法
 */
package com.kk.common.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用于返回数据的方法〉
 *
 * @author 56969
 * @create 2018/7/25
 * @since 1.0.0
 */
public class Result extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public Result() {
        this.put((String)"code", 0);
        this.put((String)"msg", "success");
    }

    public static Result error() {
        return error(500, "未知异常，请联系管理员");
    }

    public static Result error(String msg) {
        return error(500, msg);
    }

    public static Result error(int code, String msg) {
        Result r = new Result();
        r.put((String)"code", code);
        r.put((String)"msg", msg);
        // 发出异常错误
        return r;
    }

    public static Result ok(String msg) {
        Result r = new Result();
        r.put((String)"msg", msg);
        return r;
    }

    public static Result ok(Map<String, Object> map) {
        Result r = new Result();
        r.putAll(map);
        return r;
    }

    public static Result ok(Object obj) {
        Result r = new Result();
        r.put(obj);
        return r;
    }

    public static Result ok() {
        return new Result();
    }

    private void put(Object value){
        super.put("result", value);
    }

    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
