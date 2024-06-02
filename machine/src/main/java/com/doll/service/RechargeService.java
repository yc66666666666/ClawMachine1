package com.doll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doll.entity.Recharge;

public interface RechargeService extends IService<Recharge> {
    public void deleteByIds(String ids);
}
