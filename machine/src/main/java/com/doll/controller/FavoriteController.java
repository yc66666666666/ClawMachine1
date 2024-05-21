package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.doll.common.R;
import com.doll.entity.Favorite;
import com.doll.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping
    public R<String> save (@RequestBody Favorite favorite){
        favorite.setCreateTime(LocalDateTime.now());
      favoriteService.save(favorite);
      return R.success("添加收藏成功");
    }

    @DeleteMapping
    public R<String> delete(String ids){
        favoriteService.removeByIds1(ids);
        return R.success("移除收藏成功");
    }
    @GetMapping("/list")
    public R<List<Favorite>> listR(Favorite favorite){
        LambdaQueryWrapper<Favorite> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Favorite::getUserId,favorite.getUserId());
        queryWrapper.orderByDesc(Favorite::getCreateTime);
        return R.success(favoriteService.list(queryWrapper));
    }



























}
