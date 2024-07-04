package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doll.common.R;
import com.doll.dto.ComponentDto;
import com.doll.entity.Category;
import com.doll.entity.Component;
import com.doll.service.CategoryService;
import com.doll.service.ComponentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/component")
public class ComponentController {
    @Autowired
    private ComponentService componentService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
//    @PreAuthorize("hasAnyAuthority('admin','superAdmin')")
    public R<String> save(@RequestBody Component component){
        log.info(component.toString());
        componentService.save(component);
        return R.success("组件添加成功");
    }

    @GetMapping("/page")
//    @PreAuthorize("hasAnyAuthority('admin','superAdmin')")
    public R<Page> pageR(int page,int pageSize,String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
        Page<Component> page1=new Page<>(page,pageSize);
        Page<ComponentDto> componentDtoPage=new Page<>();
        LambdaQueryWrapper<Component> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Component::getName,name);
        queryWrapper.orderByDesc(Component::getUpdateTime);
        componentService.page(page1,queryWrapper);
        BeanUtils.copyProperties(page1,componentDtoPage,"records");
        List<Component> records=page1.getRecords();
        List<ComponentDto> componentDtoList=records.stream().map((item)->{
            ComponentDto componentDto=new ComponentDto();
            BeanUtils.copyProperties(item,componentDto);
            Long categoryId=item.getCategoryId();
            Category category=categoryService.getById(categoryId);
            if(category!=null){
                String categoryName=category.getName();
                componentDto.setCategoryName(categoryName);
            }
            return  componentDto;

        }).collect(Collectors.toList());
        componentDtoPage.setRecords(componentDtoList);
        return R.success(componentDtoPage);
    }
    @GetMapping("/{id}")
    public  R<Component> get(@PathVariable Long id){
        Component component=componentService.getById(id);
        return  R.success(component);
    }

    @PutMapping
    public  R<String> update(@RequestBody Component component){
        log.info(component.toString());
        componentService.updateById(component);
        return R.success("修改组件成功");
    }

    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable int status,String ids){
        componentService.changeStatus(status,ids);
        return R.success("组件的状态修改成功");
    }
     @DeleteMapping
    public R<String> delete(String ids){
        componentService.deleteByIds(ids);
        return R.success("成功删除组件");
    }



}
