package com.doll.dto;

import com.doll.entity.Commodity;
import lombok.Data;

import java.util.List;

@Data
public class CommodityDto extends Commodity {
    private String categoryName;
    private Integer copies;
}
