package com.doll.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doll.dto.ClawRecordDto;
import com.doll.dto.ExchangeToGoldDto;
import com.doll.dto.GodRankingDto;
import com.doll.entity.CaptureSuccess;
import com.doll.mapper.CaptureSuccessMapper;
import com.doll.service.CaptureSuccessService;
import com.doll.utils.PrivacyProtectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaptureSuccessServiceImpl extends ServiceImpl<CaptureSuccessMapper, CaptureSuccess> implements CaptureSuccessService {

    @Autowired
    private CaptureSuccessMapper captureSuccessMapper;

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
    public List<ExchangeToGoldDto> exchangeCoin(Long userId, int status) {
        return captureSuccessMapper.exchangeCoin(userId,status);
    }
}
