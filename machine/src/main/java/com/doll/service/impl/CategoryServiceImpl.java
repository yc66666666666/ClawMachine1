package com.doll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doll.common.CustomerException;
import com.doll.entity.Category;
import com.doll.entity.Commodity;
import com.doll.entity.Component;
import com.doll.mapper.CategoryMapper;
import com.doll.service.CategoryService;
import com.doll.service.CommodityService;
import com.doll.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ComponentService componentService;

    public void remove(Long id){
        LambdaQueryWrapper<Commodity> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Commodity::getCategoryId,id);
        int count=commodityService.count(queryWrapper);
        LambdaQueryWrapper<Component> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.eq(Component::getCategoryId,id);
        int count1=componentService.count(queryWrapper1);
        if ((count+count1)>0){
            throw  new CustomerException("当前分类关联了相关商品,禁止删除");
        }
        super.removeById(id);
    }

}
