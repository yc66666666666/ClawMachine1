package com.doll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doll.entity.Favorite;

public interface FavoriteService  extends IService<Favorite> {
    public void  removeByIds1(String ids);
}
