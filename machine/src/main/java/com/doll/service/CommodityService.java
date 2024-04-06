package com.doll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doll.entity.Commodity;

public interface CommodityService extends IService<Commodity> {
    public void removeByIds1(String id);
    public void changeStatus(int status,String ids );
}
