package com.doll.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
public class Component implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;


    //名称
    private String name;

    //组件id，如有sensor,controller,camera,
    private Long categoryId;

    //描述信息
    private String description;


     //1表示已绑定，0表示未绑定，数据库默认为0
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

