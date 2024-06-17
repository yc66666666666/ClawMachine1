package com.doll.test;

import com.doll.mapper.CaptureSuccessMapper;
import com.doll.mapper.CommodityMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class sah {

//    @Autowired
//    private RedisTemplate redisTemplate;
@Resource
    private CommodityMapper commodityMapper;
@Resource
private CaptureSuccessMapper captureSuccessMapper;

    @Test
    public void tcyjugv(){
//     System.out.println(commodityMapper.getDollOnPhoneInformation(1779064771421671426L));
     System.out.println(1+3+"ghjv"+1+3);
//        System.out.println(redisTemplate.type("myset").name()); ;

        System.out.println(captureSuccessMapper.getGodRanking(4));










    }




}


