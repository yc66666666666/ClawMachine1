package com.doll.dto;

import com.doll.entity.CaptureSuccess;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ClawRecordDto extends CaptureSuccess {

    private String  commodityName  ;//商品名称


    private String  commodityImage ; //商品图片


//    private LocalDateTime clawTime ;//抓取时间
//
//    private Integer status  ;   //状态，0为失败，1为成功,2为已退还金币,3为已驳回,默认为0

}
