package com.kk.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kk.common.vo.HttpBody;
import com.kk.common.vo.HttpMap;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;

public class HttpRequest {
    /**
     * 向指定URL发送GET方法的请求
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param userToken 用户的token
     * @return URL 所代表远程资源的响应结果
     */
	public static JSONObject sendGet(String url, String param, HttpMap userToken) throws UnsupportedEncodingException {
		RestTemplate restTemplate=new RestTemplate();
        String uri= url + "?" + param;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(userToken.getKey(), userToken.getValue());
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String strbody=restTemplate.exchange(uri, HttpMethod.GET, entity,String.class).getBody();
		return JSONUtil.objToJSONObj(strbody);
	}

    /**
     * 向目的URL发送post请求
     * @param url       目的url
     * @param params    发送的参数
     * @return {@link HttpBody} 只对单层做了判断
     */
    public static HttpBody sendPostRequest(String url, MultiValueMap<String, Object> params, HttpMap userToken) throws UnsupportedEncodingException {
        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpMethod method = HttpMethod.POST;
        // 以表单的方式提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if(userToken != null) {
            headers.set(userToken.getKey(), userToken.getValue());
        }
        //将请求头部和参数合成一个请求
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(params, headers);
        //执行HTTP请求，将返回的结构使用ResultVO类格式化
        String resStr = "";
        ResponseEntity<String> res = client.exchange(url, method, requestEntity, String.class);
        resStr = res.getBody();
        if(!resStr.contains("code")) {
            ResponseEntity<byte[]> response = client.exchange(url, method, requestEntity, byte[].class);
            // 解压数据
            resStr = GZIPUtils.uncompressToString(response.getBody());
        }
        // 数据转换
        return new Gson().fromJson(resStr, HttpBody.class);
    }
}
