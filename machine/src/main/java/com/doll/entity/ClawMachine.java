package com.doll.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ClawMachine implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;   //主键


    //名称
    private String name;




    //图片
    private String image;


    //描述信息
    private String description;


    //0 停用 1 启用
    private Integer status;


    //顺序
    private Integer sort;

    //传感器id
    private Long sensorId;

    //控制器id
    private Long controllerId;
    //摄像头id
    private Long cameraId ;

    //商品ID
    private Long commodityId;

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
