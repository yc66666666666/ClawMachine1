package com.doll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doll.dto.DollOnPhoneDto;
import com.doll.entity.Commodity;

import java.util.List;

public interface CommodityService extends IService<Commodity> {
    public void removeByIds1(String id);
    public void changeStatus(int status,String ids );

    public List<DollOnPhoneDto> getDollOnPhoneINF(String dollName,int offSet,int pageSize, Long categoryId);

}
