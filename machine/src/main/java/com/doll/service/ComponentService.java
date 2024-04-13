package com.doll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doll.entity.Component;

public interface ComponentService  extends IService<Component> {
    public void  changeStatus(int status,String ids);
    public void  deleteByIds(String ids);
}
