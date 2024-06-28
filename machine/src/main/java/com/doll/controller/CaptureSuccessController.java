package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doll.common.R;
import com.doll.dto.ClawRecordDto;
import com.doll.dto.ExchangeToGoldDto;
import com.doll.dto.GodRankingDto;
import com.doll.entity.CaptureSuccess;
import com.doll.entity.Commodity;
import com.doll.entity.User;
import com.doll.service.CaptureSuccessService;
import com.doll.service.CommodityService;
import com.doll.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/captureSuccess")
public class CaptureSuccessController {
    @Autowired
    private CaptureSuccessService captureSuccessService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommodityService commodityService;

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
    public R<Page<ExchangeToGoldDto>>  exchangeCoin(Long userId, int status,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int pageSize){
        List<ExchangeToGoldDto> exchangeToGoldDtoList=  captureSuccessService.exchangeCoin(userId,status,(page-1)*pageSize,pageSize);
        int total=captureSuccessService.getMyDollCount(userId,status);
        Page<ExchangeToGoldDto> page1=new Page<>(page,pageSize);
        page1.setRecords(exchangeToGoldDtoList);
        page1.setTotal(total);
        page1.setPages((long) Math.ceil((float)total/pageSize));
        return R.success(page1);
    }


    @PutMapping("/changeStatusAndCoin")
    @Transactional
    public R<String> changeStatus(Long captureRecordId){    //修改抓取记录的status,给用户加金币
        CaptureSuccess captureSuccess=captureSuccessService.getById(captureRecordId);
        Long userId=captureSuccess.getUserId();
        Commodity commodity= commodityService.getById(captureSuccess.getCommodityId());
        Integer value=commodity.getValue().intValue();
        CaptureSuccess captureSuccess1=new CaptureSuccess();
        captureSuccess1.setId(captureRecordId);
        captureSuccess1.setStatus(0);
        User user=new User();
        user.setId(userId);
        user.setCoin(userService.getById(userId).getCoin()+value);
        userService.updateById(user);
        captureSuccessService.updateById(captureSuccess1);
        return R.success("修改完成");
    }








}
