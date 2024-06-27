package com.doll.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doll.dto.AppealDto;
import com.doll.entity.Appeal;
import com.doll.mapper.AppealMapper;
import com.doll.service.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AppealServiceImpl extends ServiceImpl<AppealMapper, Appeal> implements AppealService {

    @Autowired
    private  AppealMapper appealMapper ;


    @Override
    public AppealDto getCommodityPrice(Long clawRecordId) {
        return appealMapper.getCommodityPrice(clawRecordId);
    }

}
