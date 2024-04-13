package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doll.common.R;
import com.doll.dto.CommodityDto;
import com.doll.entity.Category;
import com.doll.entity.Commodity;
import com.doll.service.CategoryService;
import com.doll.service.CommodityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    public R<String> save(@RequestBody Commodity commodity){
        log.info(commodity.toString());
        commodityService.save(commodity);
        return R.success("新增商品成功");
    }
    @GetMapping("/page")
    public R<Page> pageR(int page,int pageSize,String name){
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

    @GetMapping("/{id}")
    public R<Commodity> get(@PathVariable Long id){
       Commodity commodity= commodityService.getById(id);
       return  R.success(commodity);
    }


    @PutMapping
    public R<String> update(@RequestBody Commodity commodity){
        log.info(commodity.toString());
        commodityService.updateById(commodity);
        return R.success("修改菜品成功");
    }

    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable int status, String ids){
        commodityService.changeStatus(status, ids);
        return R.success("商品状态修改成功");
    }
    @DeleteMapping
    public R<String> delete(String ids){
        commodityService.removeByIds1(ids);
        return R.success("成功删除商品");
    }

}
