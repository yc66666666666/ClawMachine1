package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doll.common.R;
import com.doll.dto.ClawMachineDto;
import com.doll.entity.Category;
import com.doll.entity.ClawMachine;
import com.doll.entity.Commodity;
import com.doll.entity.Component;
import com.doll.service.CategoryService;
import com.doll.service.ClawMachineService;
import com.doll.service.CommodityService;
import com.doll.service.ComponentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/clawMachine")
public class ClawMachineController {

    @Autowired
    private ClawMachineService clawMachineService;

    @Autowired
    private ComponentService componentService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("type/{type}")
   public R getComponentAndCommodity(@PathVariable int type){
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getType,type);
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
        map.put("被抓物",commodityList);
        return R.success(map);
   }

    @GetMapping("/getLists")  //修改娃娃机时获得组件列表，默认组件放在第一个
    public R<Map> getLists(Long id){
       return clawMachineService.getBeingChangedClawMachine(id);
    }


    @PostMapping
    public R<String> save(@RequestBody ClawMachine clawMachine){
        log.info(clawMachine.toString());
//        clawMachineService.save(clawMachine);
        clawMachineService.saveMachine(clawMachine);
        return R.success("添加娃娃机成功");
    }





//    @GetMapping("/page")
//    public R<Page> pageR(int page,int pageSize,String name){
//        Page<ClawMachine> page1=new Page<>(page,pageSize);
//        LambdaQueryWrapper<ClawMachine> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.like(name!=null,ClawMachine::getName,name);
//        queryWrapper.orderByAsc(ClawMachine::getSort);
//        clawMachineService.page(page1,queryWrapper);
//        return R.success(page1);
//    }
//



    @GetMapping("/page") //管理端获得分页娃娃机
    public R<Page> pageR(int page,int pageSize,String name){
        Page<ClawMachine> page1=new Page<>(page,pageSize);
        Page<ClawMachineDto> page2=new Page<>();
        LambdaQueryWrapper<ClawMachine> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,ClawMachine::getName,name);
        queryWrapper.orderByAsc(ClawMachine::getSort);
        clawMachineService.page(page1,queryWrapper);
        BeanUtils.copyProperties(page1,page2,"records");
        List<ClawMachine> records=page1.getRecords();
        List<ClawMachineDto> dtoList=records.stream().map((item)->{
          ClawMachineDto clawMachineDto=new ClawMachineDto();
          BeanUtils.copyProperties(item,clawMachineDto);
          Long sensorId=item.getSensorId();
          Long controllerId=item.getControllerId();
          Long cameraId=item.getCameraId();
          Long commodityId=item.getCommodityId();
          Component sensor =componentService.getById(sensorId);
          Component controller=componentService.getById(controllerId);
          Component camera=componentService.getById(cameraId);
          Commodity commodity=commodityService.getById(commodityId);
          String sensorName="未配置";
          String controllerName="未配置";
          String cameraName="未配置";
          String commodityName="未选择";
          if(sensor!=null){
              sensorName=sensor.getName();
              clawMachineDto.setSensorName(sensorName);
          }
          if(controller!=null){
              controllerName=controller.getName();
              clawMachineDto.setControllerName(controllerName);
          }
          if (camera!=null){
              cameraName=camera.getName();
              clawMachineDto.setCameraName(cameraName);
          }
          if (commodity!=null){
              commodityName=commodity.getName();
              clawMachineDto.setCommodityName(commodityName);
          }
          return clawMachineDto;
        }).collect(Collectors.toList());
        page2.setRecords(dtoList);
        return R.success(page2);
    }


    @DeleteMapping
    public R<String> delete(String ids){
        log.info("被删除的分类为:{}",ids);
        clawMachineService.deleteByIds1(ids);
        return  R.success("娃娃机删除成功");
    }

    @GetMapping("/{id}")
    public R<ClawMachine> get(@PathVariable Long id){
        ClawMachine clawMachine=clawMachineService.getById(id);
        return R.success(clawMachine);
    }



    @PutMapping
    public R<String> update(@RequestBody ClawMachine clawMachine){
        log.info(clawMachine.toString());
//        clawMachineService.updateById(clawMachine);
        clawMachineService.changeMachine(clawMachine);
        return R.success("修改娃娃机成功");
    }

    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable int status,String ids){
        clawMachineService.changeStatus(status,ids);
        return R.success("娃娃机状态修改成功");
    }






}
