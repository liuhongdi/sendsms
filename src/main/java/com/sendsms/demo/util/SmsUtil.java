package com.sendsms.demo.util;

import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;

@Component
public class SmsUtil {

    @Resource
    private RedisLuaUtil redisLuaUtil;
   //发送验证码的规则：同一手机号:
    //60秒内不允许重复发送
     private static final String SEND_SECONDS = "60";
    //一天内最多发5条
     private static final String DAY_COUNT = "5";
    //密钥
    private static final String SMS_APP_SECRET = "key-thisisademonotarealappsecret";

     //发送验证码短信
    public String sendAuthCodeSms(String mobile,String authcode){

        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(
                "api",SMS_APP_SECRET));
        WebResource webResource = client.resource(
                "http://sms-api.luosimao.com/v1/send.json");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("mobile", mobile);
        formData.add("message", "验证码："+authcode+"【商城】");
        ClientResponse response =  webResource.type( MediaType.APPLICATION_FORM_URLENCODED ).
                post(ClientResponse.class, formData);
        String textEntity = response.getEntity(String.class);
        int status = response.getStatus();
        return "短信已发送";
    }

    //判断一个手机号能否发验证码短信
    public String isAuthCodeCanSend(String mobile) {
        List<String> keyList = new ArrayList();
        keyList.add(mobile);
        keyList.add(SEND_SECONDS);
        keyList.add(DAY_COUNT);
        String res = redisLuaUtil.runLuaScript("smslimit.lua",keyList);
        System.out.println("------------------lua res:"+res);

        return res;
    }

}
