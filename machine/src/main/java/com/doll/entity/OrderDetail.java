package com.doll.entity;

import lombok.Data;

import java.io.Serializable;
@Data
public class OrderDetail implements Serializable {

    private static final long serialVersionUID =1L;
    private Long id ;//订单详细信息id
    private Long orderId  ;//订单id
    private Long commodityId  ;//商品id
    private Long captureRecordId ;//商品id


}
