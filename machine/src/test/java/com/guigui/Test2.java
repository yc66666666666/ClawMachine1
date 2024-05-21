package com.guigui;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;


public class Test2 {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void tcyjugv(){

         ValueOperations valueOperations=    redisTemplate.opsForValue();
         valueOperations.set("city","厦门");
         System.out.println(valueOperations.get("city"));

//        Jedis jedis=new Jedis("xswwj.cn",6379);
//        jedis.auth("aC6LGKBtSrVrU5@");
//        System.out.println(jedis.keys("*"));
//        System.out.println(jedis.get("age"));
//        jedis.set("key","abcde");
//        System.out.println(jedis.get("key"));
//        jedis.hset("001","place","厦门");
//        System.out.println(jedis.hget("001","place"));
//        jedis.close();

    }

}
