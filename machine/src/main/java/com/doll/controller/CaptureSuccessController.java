package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doll.common.R;
import com.doll.dto.GodRankingDto;
import com.doll.entity.CaptureSuccess;
import com.doll.service.CaptureSuccessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     @GetMapping("/getGameRecording")
    public R<List<CaptureSuccess>> getGameRecords(Long userId){
        LambdaQueryWrapper<CaptureSuccess> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(CaptureSuccess::getUserId,userId);
        List<CaptureSuccess> gameRecordList=captureSuccessService.list(queryWrapper);
        return R.success(gameRecordList);
    }



}
