package com.doll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doll.common.BaseContext;
import com.doll.entity.Favorite;
import com.doll.mapper.FavoriteMapper;
import com.doll.service.FavoriteService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements FavoriteService {
    @Override
    public void removeByIds1(String ids) {
        List<Long> idList= Arrays.stream(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());
        LambdaQueryWrapper<Favorite> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId, BaseContext.getCurrentId()); //真实
//        queryWrapper.eq(Favorite::getUserId, 123456L); //测试
        queryWrapper.in(Favorite::getId,idList);
//        super.removeByIds(queryWrapper,idList);
        super.remove(queryWrapper);
    }
}
