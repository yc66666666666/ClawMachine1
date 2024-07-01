package com.doll.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doll.common.R;
import com.doll.dto.ClawRecordDto;
import com.doll.dto.ExchangeToGoldDto;
import com.doll.dto.GodRankingDto;
import com.doll.entity.CaptureSuccess;
import com.doll.entity.Commodity;
import com.doll.entity.User;
import com.doll.mapper.CaptureSuccessMapper;
import com.doll.service.CaptureSuccessService;
import com.doll.service.CommodityService;
import com.doll.service.UserService;
import com.doll.utils.PrivacyProtectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CaptureSuccessServiceImpl extends ServiceImpl<CaptureSuccessMapper, CaptureSuccess> implements CaptureSuccessService {

    @Autowired
    private CaptureSuccessMapper captureSuccessMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CommodityService commodityService;

    @Override
    public List<GodRankingDto> getGodRanking(Integer size) {
        List<GodRankingDto> godRankingDtoList= captureSuccessMapper.getGodRanking(size);
        for (GodRankingDto godRankingDto: godRankingDtoList){
            godRankingDto.setNickname(PrivacyProtectionUtils.anonymizeNickname(godRankingDto.getNickname()));
        }
        return godRankingDtoList;
    }

    @Override
    public List<GodRankingDto> getLivingGodRanking(Integer size) {
        List<GodRankingDto> LivinggodRankingDtoList= captureSuccessMapper.getLivingGodRanking(size);
        for (GodRankingDto godRankingDto: LivinggodRankingDtoList){
            godRankingDto.setNickname(PrivacyProtectionUtils.anonymizeNickname(godRankingDto.getNickname()));
        }
        return LivinggodRankingDtoList;
    }

    @Override
    public List<ClawRecordDto> getGameRecord(Long userId) {
        return captureSuccessMapper.getGameRecord(userId);
    }

    @Override
    public List<ExchangeToGoldDto> exchangeCoin(Long userId, int status ,int offset,int pageSize) {
        return captureSuccessMapper.exchangeCoin(userId,status,offset,pageSize);
    }

    @Override
    public Integer getMyDollCount(Long userId, int status) {
        return captureSuccessMapper.getMyDollCount(userId,status);
    }

    @Override
    @Transactional
    public R<String> changeStatusAndCoin(String captureRecordIds) {

        List<Long> idList = Arrays.stream(captureRecordIds.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        AtomicInteger values=new AtomicInteger(0);
        List<CaptureSuccess> captureSuccessList=new ArrayList<>();
        Long userId=null;
        for (Long captureRecordId : idList){
            CaptureSuccess captureSuccess=super.getById(captureRecordId);
            userId=captureSuccess.getUserId();
            Commodity commodity= commodityService.getById(captureSuccess.getCommodityId());
            Integer value=commodity.getValue().intValue();
            values.addAndGet(value);
            CaptureSuccess captureSuccess1=new CaptureSuccess();
            captureSuccess1.setId(Long.valueOf(captureRecordId));
            captureSuccess1.setStatus(0);
            captureSuccessList.add(captureSuccess1);
        }
        User user=new User();
        user.setId(userId);
        user.setCoin(values.addAndGet(userService.getById(userId).getCoin()));
        userService.updateById(user);
        super.updateBatchById(captureSuccessList);
        return R.success("修改完成");
    }

    @Override
    public R<String> MailDoll(Long captureRecordIds, Long addressId) {


        return null;
    }
}
