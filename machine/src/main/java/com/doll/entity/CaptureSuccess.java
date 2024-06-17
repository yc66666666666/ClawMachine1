package com.doll.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
//游戏记录表
@Data
public class CaptureSuccess implements Serializable {


    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;  //用户ID

    private Long commodityId;  //商品id

    private Long clawMachineId; //娃娃机id

    private LocalDateTime clawTime;  //抓上商品时间

    private Integer status;  //商品是否已被失效，1为正常，0为失效

    private Integer  isSuccess ; //是否抓取成功，0为失败，1为成功,2为已退还金币,3为已驳回,默认为0


}
