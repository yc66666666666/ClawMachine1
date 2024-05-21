package com.doll.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Favorite implements Serializable {

    private static final long serialVersionUID =1L;

    private Long id;
     //用户id
    private Long userId;

    //娃娃机id
    private Long clawMachineId;

    private Long commodityId ;

    //加入收藏夹时间
    private LocalDateTime createTime;

}
