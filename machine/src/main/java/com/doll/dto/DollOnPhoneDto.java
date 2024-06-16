package com.doll.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class DollOnPhoneDto implements Serializable {
    private static final long serialVersionUID = 1L;
   //商品id
   private Long commodityId ;
   //商品名称
   private  String commodityName ;
   // 顺序，前端用不到
   private Integer commoditySort ;
   //抓取所耗金币
   private BigDecimal commodityPrice ;
   //娃娃可换金币
   private BigDecimal  commodityValue ;
   //商品图片
   private String commodityImage ;
   //娃娃机id
   private Long clawMachineId ;
   //传感器id
   private  Long clawMachineSensorId ;
   //控制器id
   private Long clawMachineControllerId;
   //摄像头id
   private Long clawMachineCameraId;


}
