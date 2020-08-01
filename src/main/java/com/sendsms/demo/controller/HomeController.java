package com.sendsms.demo.controller;

import com.sendsms.demo.util.AuthCodeUtil;
import com.sendsms.demo.util.SmsUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;

@RestController
@RequestMapping("/home")
public class HomeController {

    @Resource
    private SmsUtil smsUtil;

    @Resource
    private AuthCodeUtil authCodeUtil;

    @GetMapping("/send")
    public String send(@RequestParam(value="mobile",required = true,defaultValue = "") String mobile) {

         String returnStr = "";
         String res = smsUtil.isAuthCodeCanSend(mobile);
         if (res.equals("1")) {
             //生成一个验证码
             String authcode=authCodeUtil.newAuthCode();
             //把验证码保存到缓存
             authCodeUtil.setAuthCodeCache(mobile,authcode);
             //发送短信
             return smsUtil.sendAuthCodeSms(mobile,authcode);
         } else if (res.equals("0")) {
             returnStr = "请超过60秒之后再发短信";
         } else if (res.equals("2")) {
             returnStr = "当前手机号本日内发送数量已超限制";
         }
         return returnStr;
    }

    @GetMapping("/auth")
    public String auth(@RequestParam(value="mobile",required = true,defaultValue = "") String mobile,
                       @RequestParam(value="authcode",required = true,defaultValue = "") String authcode) {

        String returnStr = "";
        String authCodeCache = authCodeUtil.getAuthCodeCache(mobile);
        System.out.println(":"+authCodeCache+":");
        if (authCodeCache.equals(authcode)) {
            returnStr = "验证码正确";
        } else {
            returnStr = "验证码错误";
        }
        return returnStr;
    }


}
