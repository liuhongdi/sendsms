package com.sendsms.demo.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class AuthCodeUtil {

    //验证码长度
    private static final int AUTHCODE_LENGTH = 6;
    //验证码的有效时间300秒
    private static final int AUTHCODE_TTL_SECONDS = 300;
    private static final String AUTHCODE_PREFIX = "AuthCode:";

    @Resource
    private RedisTemplate redisTemplate;

    //get a auth code
    public String getAuthCodeCache(String mobile){
        String authcode = (String) redisTemplate.opsForValue().get(AUTHCODE_PREFIX+mobile);
        return authcode;
    }

    //把验证码保存到缓存
    public void setAuthCodeCache(String mobile,String authcode){
        redisTemplate.opsForValue().set(AUTHCODE_PREFIX+mobile,authcode,AUTHCODE_TTL_SECONDS, TimeUnit.SECONDS);
    }

    //make a auth code
    public static String newAuthCode(){
        String code = "";
        Random random = new Random();
        for (int i = 0; i < AUTHCODE_LENGTH; i++) {
            //设置了bound参数后，取值范围为[0, bound)，如果不写参数，则取值为int范围，-2^31 ~ 2^31-1
            code += random.nextInt(10);
        }
        return code;
    }
}
