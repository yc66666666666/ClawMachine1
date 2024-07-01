package com.doll.dto;

import com.doll.entity.AddressBook;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MailOrderDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private  Long      mailOrderId;  //订单id

    private  Long      orderDetailId ; //订单详细id

    private  Long      mailOrderAddressBookId;  //邮寄地址id

    private String     commodityName  ;    //商品名

    private Long       commodityId ;      //商品名

    private String      commodityImage ;  //图片名

    private LocalDateTime  mailOrderCreateTime;  // 订单创建时间

    private AddressBook addressBook;

    private String courierNumber ;// 快递品牌及其单号




}
