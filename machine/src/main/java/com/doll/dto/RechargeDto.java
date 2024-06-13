package com.doll.dto;

import com.doll.entity.Recharge;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeDto extends Recharge {
      //最后的价格
     private BigDecimal finalPrice;
      //最后所得金币数
     private Integer finalCoin;

}
