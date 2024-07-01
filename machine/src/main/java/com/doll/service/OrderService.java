package com.doll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doll.common.R;
import com.doll.dto.ExportOrderDto;
import com.doll.dto.MailOrderDto;
import com.doll.dto.OrderDto;
import com.doll.entity.MailOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface OrderService extends IService<MailOrder> {

    public R<String> saveOrderAndDetail(OrderDto orderDto);

    public List<MailOrderDto> getOrderList( Integer status, Long userId);

    public MailOrderDto getOrderDetailList(Long orderDetailId);
    public List<ExportOrderDto> getExportOrder();

}
