package com.doll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doll.dto.DollOnPhoneDto;
import com.doll.dto.FavoriteDto;
import com.doll.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {

    List<DollOnPhoneDto> getFavoritesByUserId(@Param("userId") Long userId);

}
