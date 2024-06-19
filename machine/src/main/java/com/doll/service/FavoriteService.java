package com.doll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doll.dto.DollOnPhoneDto;
import com.doll.dto.FavoriteDto;
import com.doll.entity.Favorite;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FavoriteService  extends IService<Favorite> {
    public void  removeByIds1(String ids);

    List<DollOnPhoneDto> getFavoritesByUserId(Long userId);


}
