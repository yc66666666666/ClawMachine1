package com.doll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doll.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}
