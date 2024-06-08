package com.doll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doll.dto.DollOnPhoneDto;
import com.doll.entity.Commodity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommodityMapper extends BaseMapper<Commodity> {
//       Commodity getUserByName11(String name);
       List<DollOnPhoneDto> getDollOnPhoneInformation(@Param("dollName") String dollName,  @Param("offSet") int offSet,@Param("pageSize") int pageSize,  @Param("categoryId") Long categoryId );
}
