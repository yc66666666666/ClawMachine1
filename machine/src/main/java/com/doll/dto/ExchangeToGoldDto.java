package com.doll.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ExchangeToGoldDto  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long commodityId ;    //商品id

    private String  commodityName  ;  //商品名称

    private LocalDateTime clawTime ;  //抓取时间

    private LocalDateTime endTime ;   //过期时间

    private String  commodityValue ;  //可换金币数


}
