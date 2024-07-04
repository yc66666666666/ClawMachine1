package com.doll.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doll.common.R;
import com.doll.dto.AppealDto;
import com.doll.entity.Appeal;
import com.doll.entity.CaptureSuccess;
import com.doll.entity.User;
import com.doll.service.AppealService;
import com.doll.service.CaptureSuccessService;
import com.doll.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/appeal")
public class AppealController {

    @Autowired
    private AppealService appealService ;

    @Autowired
    private CaptureSuccessService captureSuccessService;

    @Autowired
    private UserService userService;

    @PostMapping
    public R<String> save(@RequestBody Appeal appeal){
         appealService.save(appeal);
         return R.success("申诉添加成功");
    }

    @GetMapping("/page")  //管理员权限,未审批的申诉
    @PreAuthorize("hasAnyAuthority('admin','superAdmin')")
    public R<Page<Appeal>> listR (int page ,int pageSize,String reason){
        Page<Appeal> page1=new Page<>(page,pageSize);
        LambdaQueryWrapper<Appeal> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(Appeal::getStatus,0);
        queryWrapper.like(!StringUtils.isEmpty(reason), Appeal::getAppealReason,reason);
        queryWrapper.orderByAsc(Appeal::getAppealTime);
        appealService.page(page1,queryWrapper);
        return R.success(page1);
    }

    @PutMapping("/handleAppeal")  //type为0表示驳回，1表示同意退回金币,管理员用
    @PreAuthorize("hasAnyAuthority('admin','superAdmin')")
    public  R<String> handleAppeal(Long clawRecordId,Long appealId,int type){
        if (type==0){
            CaptureSuccess captureSuccess1=new CaptureSuccess();
            captureSuccess1.setId(clawRecordId);
            captureSuccess1.setIsSuccess(3);
            captureSuccessService.updateById(captureSuccess1);
        } else if (type==1) {
            AppealDto appealDto = appealService.getCommodityPrice(clawRecordId);
            CaptureSuccess captureSuccess1=new CaptureSuccess();
            captureSuccess1.setId(clawRecordId);
            captureSuccess1.setIsSuccess(2);
            captureSuccessService.updateById(captureSuccess1);

            User user=new User();
            user.setId(appealDto.getUserId());
            user.setCoin(appealDto.getPrice().intValue()+userService.getById(appealDto.getUserId()).getCoin());
            userService.updateById(user);
        }

        Appeal appeal=new Appeal();
        appeal.setId(appealId);
        appeal.setStatus(1);
        appealService.updateById(appeal);
        return R.success("申诉处理完成");

    }




}
