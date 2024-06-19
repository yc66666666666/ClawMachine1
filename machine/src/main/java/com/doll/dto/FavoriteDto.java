package com.doll.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FavoriteDto {
    private static final long serialVersionUID = 1L;

    //商品图片
    private String  commodityImage ;

    //商品名称
    private String commodityName;

    //抓一次所画金币数
    private BigDecimal commodityPrice ;

    //商品id
    private Long  commodityId;

}
