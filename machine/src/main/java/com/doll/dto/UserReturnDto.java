package com.doll.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.doll.entity.User;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserReturnDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //用户名
    private String username;

    //手机号
    private String phone;


    //性别 0 女 1 男
    private String sex;


    //邮箱
    private String email;

    //头像
    private String avatar;

    //昵称
    private String nickname ;

    //微信唯一认证
    private Long wechatId;

    //用户角色
    private String role;

    //现有金币数
    private Integer coin;

    //最后登录时间
    private LocalDateTime latestLoginTime;

    private String jwt;


}
