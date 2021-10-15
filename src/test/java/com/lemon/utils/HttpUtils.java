package com.lemon.utils;

import com.alibaba.fastjson.JSONObject;
//import com.lemon.EncryptUtils;
import com.lemon.cases.LoginCase;
//import com.lemon.encryption.EncryptUtils;
import com.lemon.pojo.CaseInfo;
import io.restassured.RestAssured;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * @Project: api_auto_v2
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: 2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: luojie
 * @Create: 2020-09-05 09:43
 * @Desc： 发送http请求
 **/
public class HttpUtils {


    public static void main(String[] args) {
        String jsonData = "{\"mobilephone\":\"13323234545\",\"pwd\":\"123456\"}";
        Response res =
                given().
                        contentType(ContentType.JSON).
                        body(jsonData).
                        when().
                        post("http://httpbin.org/post").
                        then().
                        extract().response();
        //获取接口请求响应时间
        System.out.println(res.time());
        System.out.println(res.asString());
        //获取响应头信息
        System.out.println(res.getHeader("Content-Type"));
        //获取响应体信息(Json格式)
        Object o = res.jsonPath().get("headers.Host");
        System.out.println(o);
    }

    private static Logger logger = Logger.getLogger(HttpUtils.class);


    /**
     * 在json参数中添加v3鉴权参数：timestamp，sign
     * @param caseInfo            caseInfo
     * @return
     */
    public static void authorizationParam(CaseInfo caseInfo) {
//        //timestamp 参数
//        long timestamp = System.currentTimeMillis() / 1000;
//        String token = UserData.vars.get("${token}").toString();
//        String token50 = token.substring(0,50);
//        //token前50位和timestamp相加
//        String sign = token50 + timestamp;
//        //sign参数 rsa加密
//        sign = EncryptUtils.rsaEncrypt(sign);
//        //params => Map
//        Map map = JSONObject.parseObject(caseInfo.getParams(), Map.class);
//        map.put("timestamp",timestamp);
//        map.put("sign",sign);
//        //添加完参数之后转会Json串
//        String params = JSONObject.toJSONString(map);
//        caseInfo.setParams(params);
    }

    /**
     * 返回默认请求头Map
     * @return
     */
    public static Map<String,Object> getDefaultHeaders() {
        Map<String,Object> headers = new HashMap<>();
        headers.put("X-Lemonban-Media-Type", Constants.X_LEMONBAN_MEDIA_TYPE);
        headers.put("Content-Type", "application/json");
        return headers;
    }

    /**
     * 获取带有鉴权信息和默认信息的头
     * @return
     */
    public static Map<String,Object> getAuthorizationHeaders() {
        Map<String, Object> headers = getDefaultHeaders();
        //取出token，存到headers中
        Object token = UserData.vars.get("${token}");
        System.out.println("Bearer " + token);
        headers.put("Authorization","Bearer " + token);
        return headers;
    }

    /**
     * http请求方法,并且输出响应体
     * @param caseInfo      请求参数
     * @param headers       请求头
     * @return              响应体
     */
    public static String call(CaseInfo caseInfo,Map<String,Object> headers) {
        //1、获取请求参数
        String params = caseInfo.getParams();
        String contentType = caseInfo.getContentType();
        String type = caseInfo.getType();
        String url = caseInfo.getUrl();
        String body = null;
        //2、判断请求类型
        logger.info("请求url：" + url);
        logger.info("请求头：" + headers);
        logger.info("请求参数：" + params);
        //获取时间戳


        if("post".equalsIgnoreCase(type)) {
            //2.1、如果是form类型参数的接口
            if("form".equalsIgnoreCase(contentType)) {
                params = json2KeyValue(params);
                //指定Content-Type为form类型参数
                headers.put("Content-Type", "application/x-www-form-urlencoded");
            }
            body = HttpUtils.myPost(url, headers, params);
        }else if("get".equalsIgnoreCase(type)) {
            body = HttpUtils.myGet(url, headers);
        }else if("patch".equalsIgnoreCase(type)) {
            body = HttpUtils.myPatch(url, headers, params);
        }
        logger.info("响应体：" + body);
        return body;
    }

    /**
     * json字符串转成key=value字符串
     * 例子：{"mobilephone":"13877788811","pwd":"12345678"} => mobilephone=13877788811&pwd=12345678
     * @param params        json字符粗
     * @return
     */
    private static String json2KeyValue(String params) {
        //2.2、json参数转成 jsonStr => map => keyValueStr
        //JSON转成map
        Map<String,String> map = JSONObject.parseObject(params, Map.class);
        //获取所有的key
        Set<String> keySet = map.keySet();
        String formParams = "";
        for (String key : keySet) {
            //key=value&
            formParams += key + "=" + map.get(key) + "&";
        }
        //删除最后一个字符串
        formParams = formParams.substring(0,formParams.length()-1);
        System.out.println(formParams);
        //参数重新赋值
        params = formParams;
        return params;
    }


    /**
     * get请求
     * @param url               接口地址+参数。格式：url?pageSize=1  格式:member/11/info
     * @param headers           请求头
     * @return                  响应体，如果需要响应头，不调用asString,返回response
     */
    public static String myGet(String url, Map<String,Object> headers) {
        return given().headers(headers).get(url).asString();
    }

    /**
     * post请求
     * @param url               接口地址
     * @param headers           请求头
     * @param params            json参数：{id:11,name:"ZS"}   form参数：id=123&name=zs
     * @return                  响应体，如果需要响应头，不调用asString,返回response
     */
    public static String myPost(String url, Map<String,Object> headers,String params) {
        return given().headers(headers).body(params)
                .post(url) .asString();
    }

    /**
     * patch请求
     * @param url               接口地址
     * @param headers           请求头
     * @param params            json参数：{id:11,name:"ZS"}
     * @return                  响应体，如果需要响应头，不调用asString,返回response
     */
    public static String myPatch(String url, Map<String,Object> headers,String params) {
        return given().headers(headers).body(params).patch(url).asString();
    }

}
