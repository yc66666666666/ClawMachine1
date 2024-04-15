package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doll.common.R;
import com.doll.entity.ClawMachine;
import com.doll.service.ClawMachineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/clawMachine")
public class ClawMachineController {

    @Autowired
    private ClawMachineService clawMachineService;

    @PostMapping
    public R<String> save(@RequestBody ClawMachine clawMachine){
        log.info(clawMachine.toString());
//        clawMachineService.save(clawMachine);
        clawMachineService.saveMachine(clawMachine);
        return R.success("添加娃娃机成功");
    }


    @GetMapping("/page")
    public R<Page> pageR(int page,int pageSize,String name){
        Page<ClawMachine> page1=new Page<>(page,pageSize);
        LambdaQueryWrapper<ClawMachine> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,ClawMachine::getName,name);
        queryWrapper.orderByAsc(ClawMachine::getSort);
        clawMachineService.page(page1,queryWrapper);
        return R.success(page1);
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
        clawMachineService.updateById(clawMachine);
        return R.success("修改娃娃机成功");
    }

    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable int status,String ids){
        clawMachineService.changeStatus(status,ids);
        return R.success("娃娃机状态修改成功");
    }


}
