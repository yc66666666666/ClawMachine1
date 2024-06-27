package com.doll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doll.dto.ClawRecordDto;
import com.doll.dto.ExchangeToGoldDto;
import com.doll.dto.GodRankingDto;
import com.doll.entity.CaptureSuccess;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CaptureSuccessService extends IService<CaptureSuccess> {

    List<GodRankingDto> getGodRanking(Integer size);
    List<GodRankingDto> getLivingGodRanking(Integer size);
    List<ClawRecordDto> getGameRecord(Long userId);

    List<ExchangeToGoldDto> exchangeCoin(Long userId,  int status);
}
