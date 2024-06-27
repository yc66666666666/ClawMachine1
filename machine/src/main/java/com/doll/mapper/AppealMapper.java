package com.doll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doll.dto.AppealDto;
import com.doll.entity.Appeal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface AppealMapper extends BaseMapper<Appeal> {


    public AppealDto getCommodityPrice(@Param("clawRecordId") Long clawRecordId);


}
