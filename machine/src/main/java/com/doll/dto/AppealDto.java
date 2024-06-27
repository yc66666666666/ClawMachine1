package com.doll.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class AppealDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal price;

    private Long userId ;


}
