package com.doll.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doll.entity.Commodity;
import com.doll.mapper.CommodityMapper;
import com.doll.service.CategoryService;
import com.doll.service.CommodityService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

    @Override
    public void removeByIds1(String id) {
        List<Long> idList = Arrays.stream(id.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
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
