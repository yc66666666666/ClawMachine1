package com.doll.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Appeal implements Serializable {


    private static final long serialVersionUID = 1L;

    private Long   id  ;

    private Long   clawRecordId;  //游戏记录id

    private String  appealReason ; //申诉原因

    private String description ;//具体描述信息

    private String  image   ;//图片凭证

    private Integer status ; // '0 未处理,1已处理

    private LocalDateTime appealTime ; //申诉时间

}
