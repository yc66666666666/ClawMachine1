package com.doll.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GodRankingDto  implements Serializable {
    private static final long serialVersionUID = 1L;

    private String avatar ;

    private String nickname ;

    private Integer dollCount ;


}
