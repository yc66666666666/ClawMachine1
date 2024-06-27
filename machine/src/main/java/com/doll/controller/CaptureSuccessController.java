package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doll.common.R;
import com.doll.dto.ClawRecordDto;
import com.doll.dto.ExchangeToGoldDto;
import com.doll.dto.GodRankingDto;
import com.doll.entity.CaptureSuccess;
import com.doll.service.CaptureSuccessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/captureSuccess")
public class CaptureSuccessController {
    @Autowired
    private CaptureSuccessService captureSuccessService;

    @GetMapping("/getGodRanking")  //获得上周大神榜
     public R<Page<GodRankingDto>>  getGodRanking(){
        List<GodRankingDto> godRankingDtoList=  captureSuccessService.getGodRanking(20);
        int length=godRankingDtoList.size();
        Page<GodRankingDto> page=new Page<>(0,0);
        page.setRecords(godRankingDtoList);
        page.setTotal(length);
        return R.success(page);
    }

    @GetMapping("/getLivingGodRanking")  //获得本周大神榜
    public R<Page<GodRankingDto>>  getLivingGodRanking(){
        List<GodRankingDto> godRankingDtoList= captureSuccessService.getLivingGodRanking(30);
        int length=godRankingDtoList.size();
        Page<GodRankingDto> page=new Page<>(0,0);
        page.setRecords(godRankingDtoList);
        page.setTotal(length);
        return R.success(page);
    }
     @GetMapping("/getGameRecording")  //通过用户id获得用户游戏记录
    public R<List<ClawRecordDto>> getGameRecords(Long userId){
        return R.success(captureSuccessService.getGameRecord(userId));
    }
    @PostMapping   //添加游戏记录，返回游戏记录id
    public R<Long> saveRecord(@RequestBody CaptureSuccess captureSuccess){
        captureSuccessService.save(captureSuccess);
        return R.success(captureSuccess.getId());
    }

    @PutMapping("/changeStatus")
    public R<String> changeToSuccess(@RequestBody CaptureSuccess captureSuccess){
        captureSuccess.setIsSuccess(captureSuccess.getStatus());
        captureSuccess.setId(captureSuccess.getId());
        captureSuccess.setStatus(null);
        captureSuccessService.updateById(captureSuccess);
        return R.success("成功");
    }

    @GetMapping("/changeToCoin")
    public R<Page<ExchangeToGoldDto>>  exchangeCoin(Long userId, int status){
        List<ExchangeToGoldDto> exchangeToGoldDtoList=  captureSuccessService.exchangeCoin(userId,status);
        Page<ExchangeToGoldDto> page=new Page<>(0,0);
        page.setRecords(exchangeToGoldDtoList);
        page.setPages(1);
        page.setCurrent(0);
        page.setTotal(exchangeToGoldDtoList.size());
        return R.success(page);
    }





}
