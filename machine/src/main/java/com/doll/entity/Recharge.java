package com.doll.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * 充值信息
 */
@Data
public class Recharge {

    private static final long serialVersionUID = 1L;

    private Long id;

    //充值卡名称
    private String name;


    //充值价格
    private BigDecimal price;

    //充值所值金币
    private Integer worthCoin  ;

    //赠送金币
    private Integer  presentCoin ;

    //折扣
    private BigDecimal discount;

    //默认为0，0什么都不送，1表示送一张包邮券
    private Integer type;


    //描述信息
    private String description;


    //0 停用 1 起用,默认为0
    private Integer status;


    //顺序
    private Integer sort;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    @TableField(fill = FieldFill.INSERT)
    private Long createUser;


    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    //是否删除
    @TableLogic
    private Integer isDeleted;



}
