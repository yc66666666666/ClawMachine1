package com.doll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doll.common.R;
import com.doll.entity.Category;
import com.doll.entity.ClawMachine;
import com.doll.entity.Commodity;
import com.doll.entity.Component;
import com.doll.mapper.CategoryMapper;
import com.doll.mapper.ClawMachineMapper;
import com.doll.service.CategoryService;
import com.doll.service.ClawMachineService;
import com.doll.service.CommodityService;
import com.doll.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.management.Sensor;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClawMachineServiceImpl extends ServiceImpl<ClawMachineMapper, ClawMachine> implements ClawMachineService {

    @Autowired
    private ComponentService componentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommodityService commodityService;





    @Override
    @Transactional
    public void saveMachine(ClawMachine clawMachine) {
        List<Long> list=new ArrayList<>();
        Long sensorId=clawMachine.getSensorId();
        Long controllerId=clawMachine.getControllerId();
        Long cameraId=clawMachine.getCameraId();
        list.add(sensorId);
        list.add(controllerId);
        list.add(cameraId);
        String ids=list.stream().map(String::valueOf).collect(Collectors.joining(","));
        componentService.changeStatus(1,ids);
        super.save(clawMachine);
    }

    @Override
    @Transactional
    public void deleteByIds1(String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        for(Long id:idList){
            ClawMachine clawMachine=super.getById(id);
            if(clawMachine!=null){
                List<Long> list=new ArrayList<>();
                Long sensorId=clawMachine.getSensorId();
                Long controllerId=clawMachine.getControllerId();
                Long cameraId=clawMachine.getCameraId();
                list.add(sensorId);
                list.add(controllerId);
                list.add(cameraId);
                String componentIds=list.stream().map(String::valueOf).collect(Collectors.joining(","));
                componentService.changeStatus(0,componentIds);
            }
        }
        super.removeByIds(idList);
    }

    @Override
    public void changeStatus(int status, String ids) {
        List<Long> idList=Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<ClawMachine> clawMachineList=new ArrayList<>();
        for(Long id:idList){
            ClawMachine clawMachine=new ClawMachine();
            clawMachine.setStatus(status);
            clawMachine.setId(id);
            clawMachineList.add(clawMachine);
        }
        super.updateBatchById(clawMachineList);
    }

    @Override
    public R<Map>  getBeingChangedClawMachine(Long id){
        ClawMachine clawMachine=super.getById(id);
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getType,2);
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categoryList=categoryService.list(queryWrapper);
        Map<String,List> map=new HashMap();
        for (Category category:categoryList){
            LambdaQueryWrapper<Component> queryWrapper1=new LambdaQueryWrapper<>();
            queryWrapper1.eq(Component::getCategoryId,category.getId()).eq(Component::getStatus,0);
            List<Component> componentList= componentService.list(queryWrapper1);
            map.put(category.getName(),componentList);
        }
        LambdaQueryWrapper<Commodity> queryWrapper2=new LambdaQueryWrapper<>();
        queryWrapper2.eq(Commodity::getStatus,1);
        List<Commodity> commodityList=commodityService.list(queryWrapper2);
        Commodity commodity=commodityService.getById(clawMachine.getCommodityId());
        commodityList.add(0,commodity);
        map.put("被抓物",commodityList);
        Component sensor=componentService.getById(clawMachine.getSensorId());
        Component controller=componentService.getById(clawMachine.getControllerId());
        Component camera=componentService.getById(clawMachine.getCameraId());
        String sensorName= categoryService.getById(sensor.getCategoryId()).getName();
        String controllerName=categoryService.getById(controller.getCategoryId()).getName();
        String cameraName=categoryService.getById(camera.getCategoryId()).getName();
        map.get(sensorName).add(0,sensor);
        map.get(controllerName).add(0,controller);
        map.get(cameraName).add(0,camera);
        return R.success(map);
    }

    @Override
    @Transactional
    public void changeMachine(ClawMachine clawMachine) {

        ClawMachine clawMachine1=super.getById(clawMachine.getId());
        Long sensorId=clawMachine1.getSensorId();
        Long controllerId=clawMachine1.getControllerId();
        Long cameraId=clawMachine1.getCameraId();
        List<Long> list=new ArrayList<>();
        List<Component> componentList=new ArrayList<>();
        list.add(sensorId);
        list.add(controllerId);
        list.add(cameraId);
        for(Long id :list){
            Component component=new Component();
            component.setId(id);
            component.setStatus(0);
            componentList.add(component);
        }


//        list.add(sensorId);
//        list.add(controllerId);
//        list.add(cameraId);
//        String componentIds=list.stream().map(String::valueOf).collect(Collectors.joining(",")); //改之前的
//        componentService.changeStatus(0,componentIds);

        Long sensorId1=clawMachine.getSensorId();
        Long controllerId1=clawMachine.getControllerId();
        Long cameraId1=clawMachine.getCameraId();
        List<Long> list1=new ArrayList<>();
        list1.add(sensorId1);
        list1.add(controllerId1);
        list1.add(cameraId1);
        for (Long id1: list1){
            Component component=new Component();
            component.setId(id1);
            component.setStatus(1);
            componentList.add(component);
        }
//        String componentIds1=list.stream().map(String::valueOf).collect(Collectors.joining(",")); //改之后的
//        componentService.changeStatus(1,componentIds1);
        componentService.updateBatchById(componentList);
        super.updateById(clawMachine);

    }

}
