package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.doll.common.R;
import com.doll.entity.Recharge;
import com.doll.service.RechargeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/recharge")
public class RechargeController {

    @Autowired
    private RechargeService rechargeService;

    @PostMapping
    public R<String> save(@RequestBody Recharge recharge){
        rechargeService.save(recharge);
        return R.success("添加充值卡成功");
    }

    @GetMapping("/list")
    public R<List<Recharge>> getRecharge(){
        LambdaQueryWrapper<Recharge> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Recharge::getPrice);
//        queryWrapper.eq(Recharge::getStatus,1);
        List<Recharge> rechargeList= rechargeService.list(queryWrapper);
        return R.success(rechargeList);
    }

    @GetMapping("/{id}")
    public R<Recharge> getOneById(@PathVariable Long id){
          Recharge recharge=rechargeService.getById(id);
          return R.success(recharge);
    }

    @PutMapping
    public R<String> update(@RequestBody Recharge recharge){
        rechargeService.updateById(recharge);
        return R.success("充值卡信息修改成功");
    }

    @DeleteMapping
    public R<String> delete(String ids){
        rechargeService.deleteByIds(ids);
        return R.success("充值卡删除成功");
    }





}
