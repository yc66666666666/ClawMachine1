package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doll.common.R;
import com.doll.dto.CommodityDto;
import com.doll.entity.Category;
import com.doll.entity.ClawMachine;
import com.doll.entity.Commodity;
import com.doll.service.CategoryService;
import com.doll.service.ClawMachineService;
import com.doll.service.CommodityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class CommodityController {
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ClawMachineService clawMachineService;

    @Autowired
    private CacheManager cacheManager;

    //CachePut:将方法返回值放入缓存
//    @CachePut(value = "commodityCache",key = "#commodity.id")
    @CacheEvict(value = "commodityCache" ,allEntries = true)
    @PostMapping
    public R<String> save(@RequestBody Commodity commodity){
        log.info(commodity.toString());
//        commodity.setValue(BigDecimal.valueOf(12)); //测试用
        commodityService.save(commodity);
        return R.success("新增商品成功");
    }
     //管理员查询
    @Cacheable(value = "commodityCache",key = "'admin_'+#page+'_'+#pageSize+'_'+#name",unless = "#result.data==null")
    @GetMapping("/page")
    public R<Page> pageAdmin(int page,int pageSize,String name){
        Page<Commodity> pageInfo=new Page<>(page,pageSize);
        Page<CommodityDto> commodityDtoPage=new Page<>();
        LambdaQueryWrapper<Commodity> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Commodity::getName,name);
        queryWrapper.orderByDesc(Commodity::getUpdateTime).orderByDesc(Commodity::getCategoryId);
        commodityService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,commodityDtoPage,"records");
        List<Commodity> records=pageInfo.getRecords();
        List<CommodityDto> list=records.stream().map((item)->{
           CommodityDto commodityDto=new CommodityDto() ;
           BeanUtils.copyProperties(item,commodityDto);
           Long categoryId=item.getCategoryId();
           Category category=categoryService.getById(categoryId);
           if(category!=null){
               String categoryName=category.getName();
               commodityDto.setCategoryName(categoryName);
           }
           return commodityDto;
        }).collect(Collectors.toList());
        commodityDtoPage.setRecords(list);
        return R.success(commodityDtoPage);
    }
    //用户查询,name为娃娃的模糊名字，categoryId为娃娃的种类
//    @Cacheable(value = "commodityCache",key = "'user_'+#page+'_'+#pageSize+'_'+#name",unless = "#result.data==null")
    @GetMapping("/pageUser")
    public R<Page> pageUser(int page,int pageSize,String name,Long categoryId){

        LambdaQueryWrapper<Commodity> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.like(name!=null,Commodity::getName,name);
        queryWrapper.eq(Commodity::getStatus,1);
        queryWrapper.eq(categoryId!=null,Commodity::getCategoryId,categoryId);
//        queryWrapper.orderByAsc(Commodity::getSort);
        List<Commodity> commodityList =commodityService.list(queryWrapper);
        List<Long> commodityIds=new ArrayList<>();
        int len=commodityList.size();
        for (int i=0;i<len;i++){
           commodityIds.add(commodityList.get(i).getId());
        }
        Page<ClawMachine> page1=new Page<>(page,pageSize);
        LambdaQueryWrapper<ClawMachine> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(ClawMachine::getStatus,1);
        queryWrapper1.in(ClawMachine::getCommodityId,commodityIds);
        queryWrapper1.orderByAsc(ClawMachine::getSort);
        clawMachineService.page(page1,queryWrapper1);
        return R.success(page1);
    }

    //Cacheable有数据则直接调用，如果没有将返回的数据放入缓存
    @Cacheable(value = "commodityCache",key = "#id",unless = "#result.data==null")
    @GetMapping("/{id}")
    public R<Commodity> get(@PathVariable Long id){
       Commodity commodity= commodityService.getById(id);
       System.out.println("111111"+commodity);
       return  R.success(commodity);
    }

//    @CacheEvict(value ="commodityCache",key = "#commodity.id" )
    @CacheEvict(value = "commodityCache" , allEntries = true)
    @PutMapping
    public R<String> update(@RequestBody Commodity commodity){
        log.info(commodity.toString());
        commodityService.updateById(commodity);
        return R.success("修改菜品成功");
    }

    @CacheEvict(value = "commodityCache" , allEntries = true)
    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable int status, String ids){
        commodityService.changeStatus(status, ids);
        return R.success("商品状态修改成功");
    }

//    @CacheEvict(value = "commodityCache",key = "#ids")
    @CacheEvict(value = "commodityCache",allEntries = true)
    @DeleteMapping
    public R<String> delete(String ids){
        commodityService.removeByIds1(ids);
        return R.success("成功删除商品");
    }

}
