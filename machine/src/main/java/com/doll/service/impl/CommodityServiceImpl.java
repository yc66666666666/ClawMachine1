package com.doll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doll.common.CustomerException;
import com.doll.entity.ClawMachine;
import com.doll.entity.Commodity;
import com.doll.mapper.CommodityMapper;
import com.doll.service.CategoryService;
import com.doll.service.ClawMachineService;
import com.doll.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

    @Autowired
    private ClawMachineService clawMachineService;

    @Override
    public void removeByIds1(String id) {
        List<Long> idList = Arrays.stream(id.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        
        LambdaQueryWrapper<ClawMachine> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(ClawMachine::getCommodityId,idList);

        int count=clawMachineService.count(queryWrapper);
        if (count>0){
            throw  new CustomerException("当前商品关联了相关娃娃机，禁止删除");
        }
        super.removeByIds(idList);

    }

    @Override
    public void changeStatus(int status, String id) {
        List<Long> idList = Arrays.stream(id.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Commodity> commodityList=new ArrayList<>();
        for (int i=0;i<idList.size();i++){
            Commodity commodity=new Commodity();
            commodity.setStatus(status);
            commodity.setId(idList.get(i));
            commodityList.add(commodity);
        }
        super.updateBatchById(commodityList);
    }
}
