package com.doll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doll.dto.AppealDto;
import com.doll.entity.Appeal;

import java.math.BigDecimal;

public interface AppealService  extends IService<Appeal> {

    public AppealDto getCommodityPrice(Long clawRecordId);

}
