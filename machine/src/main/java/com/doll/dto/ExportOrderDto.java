package com.doll.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ExportOrderDto implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long  mailOrderId;  //订单id

    private Long  commodityId;  //商品id

    private String commodityName ;//商品名

    private String   addressBookConsignee; //收货人

    private String address;  //地址

    private String  addressBookDetail ; //详细地址

    private String   mailOrderRemark ;  //备注

    private LocalDateTime  mailOrderCreateTime; //下单时间


}
