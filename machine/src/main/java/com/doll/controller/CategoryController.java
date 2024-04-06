package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doll.common.R;
import com.doll.entity.Category;
import com.doll.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("添加分类{}",category.toString());
        categoryService.save(category);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public  R<Page> pageR(int page,int pageSize){
        Page<Category> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return  R.success(pageInfo);
    }
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("被删除的分类id为:{}",ids);
//        categoryService.removeById(id);
        categoryService.remove(ids);
        return  R.success("分类删除成功");
    }
    @PutMapping
    public  R<String> update(@RequestBody Category category){
      log.info("修改了分类的信息为{}",category);
      categoryService.updateById(category);
      return R.success("修改分类信息成功");

    }
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list=categoryService.list(queryWrapper);
        return R.success(list);
    }


}
