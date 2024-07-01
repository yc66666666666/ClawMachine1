package com.doll.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doll.dto.MailOrderDto;
import com.doll.entity.MailOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<MailOrder> {

    public List<MailOrderDto> getOrderList(@Param("status") Integer status, @Param("userId") Long userId );

    public MailOrderDto getOrderDetailList(@Param("orderDetailId") Long orderDetailId);



}
