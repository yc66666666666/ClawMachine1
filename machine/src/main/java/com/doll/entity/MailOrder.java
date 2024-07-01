package com.doll.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class MailOrder implements Serializable {

    private static final long serialVersionUID =1L;

    private Long  id ;  //订单id

    private Long userId  ; //用户id

    private Long addressBookId ; //地址id

    private LocalDateTime createTime ; //创建订单时间

    private Integer  status ;   //0 未发货 1 已发货 2 已收货 3 已取消

    private String  remark ; //备注

    private String courierNumber ;// 快递品牌及其单号


}
