package com.doll.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doll.entity.Commodity;
import com.doll.entity.Component;
import com.doll.mapper.ComponentMapper;
import com.doll.service.CommodityService;
import com.doll.service.ComponentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComponentServiceImpl extends ServiceImpl<ComponentMapper, Component> implements ComponentService {
    @Override
    public void changeStatus(int status, String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Component> componentList=new ArrayList<>();
        for (int i=0;i<idList.size();i++){
            Component component=new Component();
            component.setStatus(status);
            component.setId(idList.get(i));
            componentList.add(component);
        }
        super.updateBatchById(componentList);
    }

    @Override
    public void deleteByIds(String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        super.removeByIds(idList);
    }
}
