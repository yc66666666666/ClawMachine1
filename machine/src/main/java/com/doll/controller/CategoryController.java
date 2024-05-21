package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doll.common.R;
import com.doll.entity.Category;
import com.doll.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("添加分类{}",category.toString());
        categoryService.save(category);
        Set keys=redisTemplate.keys("category*");
        redisTemplate.delete(keys);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public  R<Page> pageR(int page,int pageSize){
        String key = String.format("categoryAll%s%s", page,pageSize);
        R<Page> p= (R<Page>) redisTemplate.opsForValue().get(key);
        if (p!=null){   //如果redis有缓存
         return p;
        }
        Page<Category> pageInfo=new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.orderByAsc(Category::getSort);
        queryWrapper.orderByAsc(Category::getType).orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        redisTemplate.opsForValue().set(key,R.success(pageInfo),60, TimeUnit.MINUTES);
        return  R.success(pageInfo);
    }
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("被删除的分类id为:{}",ids);
//        categoryService.removeById(id);
        categoryService.remove(ids);
        Set keys=redisTemplate.keys("category*");
        redisTemplate.delete(keys);
        return  R.success("分类删除成功");
    }
    @PutMapping
    public  R<String> update(@RequestBody Category category){
      log.info("修改了分类的信息为{}",category);
      categoryService.updateById(category);
      Set keys=redisTemplate.keys("category*");
      redisTemplate.delete(keys);
      return R.success("修改分类信息成功");

    }
    @GetMapping("/list")  //根据type查询category
    public R<List<Category>> list(Category category){
        if(category.getType()==null){
            return R.error("没有对应的分类");
        }
        String key=String.format("category%s",category.getType());
        List<Category> categoryList=(List<Category>) redisTemplate.opsForValue().get(key);
        if(categoryList!=null){
            return R.success(categoryList);
        }
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list=categoryService.list(queryWrapper);
        redisTemplate.opsForValue().set(key,list,60,TimeUnit.MINUTES);
        return R.success(list);
    }


}
