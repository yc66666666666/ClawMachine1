package com.doll.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充insert");

            metaObject.setValue("createTime",LocalDateTime.now());
            metaObject.setValue("updateTime",LocalDateTime.now());
//        metaObject.setValue("updateUser",BaseContext.getCurrentId()); //实际
//       metaObject.setValue("createUser",BaseContext.getCurrentId());  //实际
            metaObject.setValue("updateUser",16L);  //测试
            metaObject.setValue("createUser",16L);  //测试


    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充update");
            metaObject.setValue("updateTime",LocalDateTime.now());
//        metaObject.setValue("updateUser",BaseContext.getCurrentId()); //实际
            metaObject.setValue("updateUser",16L);  //测试



    }
}
