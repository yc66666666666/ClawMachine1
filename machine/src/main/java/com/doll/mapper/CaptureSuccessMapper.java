package com.doll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doll.dto.ClawRecordDto;
import com.doll.dto.GodRankingDto;
import com.doll.entity.CaptureSuccess;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CaptureSuccessMapper extends BaseMapper<CaptureSuccess> {

    List<GodRankingDto> getGodRanking(@Param("size") Integer size);

    List<GodRankingDto> getLivingGodRanking(@Param("size") Integer size);

    List<ClawRecordDto> getGameRecord(@Param("userId") Long userId);
}
