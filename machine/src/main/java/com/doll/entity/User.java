package com.doll.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import java.time.LocalDateTime;
import java.io.Serializable;
/**
 * 用户信息
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //昵称
    private String name;


    //手机号
    private String phone;


    //性别 0 女 1 男
    private String sex;


    //身份证号
    private String idNumber;


    //头像
    private String avatar;

     //用户的现有金币数
    private Integer  coin ;

    //状态 0:禁用，1:正常
    private Integer status;
    
    //注册时间
    private LocalDateTime registrationTime;

    //最后登录时间
    private LocalDateTime latestLoginTime;

    //是否删除
    @TableLogic
    private Integer isDeleted;    

}
