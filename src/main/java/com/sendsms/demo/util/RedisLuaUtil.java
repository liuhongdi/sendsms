package com.sendsms.demo.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RedisLuaUtil {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    //private static final Logger logger = LogManager.getLogger("bussniesslog");
    /*
    run a lua script
    luaFileName: lua file name,no path
    keyList: list for redis key
    return 0: fail
           1: success
    */
    public String runLuaScript(String luaFileName, List<String> keyList) {
        DefaultRedisScript<String> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/"+luaFileName)));
        redisScript.setResultType(String.class);
        String result = "";
        String argsone = "none";
        try {
            result = stringRedisTemplate.execute(redisScript, keyList,argsone);
        } catch (Exception e) {
            //logger.error("发生异常",e);
        }
        return result;
    }
}