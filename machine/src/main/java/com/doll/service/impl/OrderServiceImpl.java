package com.doll.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doll.common.R;
import com.doll.dto.ExportOrderDto;
import com.doll.dto.MailOrderDto;
import com.doll.dto.OrderDto;
import com.doll.entity.CaptureSuccess;
import com.doll.entity.MailOrder;
import com.doll.entity.OrderDetail;
import com.doll.mapper.OrderMapper;
import com.doll.service.CaptureSuccessService;
import com.doll.service.OrderDetailService;
import com.doll.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, MailOrder> implements OrderService {
    @Autowired
    private CaptureSuccessService captureSuccessService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    @Transactional
    public R<String> saveOrderAndDetail(OrderDto orderDto) {
        List<Long> captureRecordIds = Arrays.stream(orderDto.getCaptureRecordIds().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        //保存订单
        orderDto.setStatus(0);
        orderDto.setUserId(captureSuccessService.getById(captureRecordIds.get(0)).getUserId());
        MailOrder order=new MailOrder();
        BeanUtils.copyProperties(orderDto,order);
        this.save(order);



        List<OrderDetail> orderDetailList=new ArrayList<>();
        List<CaptureSuccess> captureSuccessList=new ArrayList<>();
        //保存订单细节
        for (Long captureRecordId : captureRecordIds ){
            OrderDetail orderDetail=new OrderDetail();
            orderDetail.setOrderId(order.getId());
            orderDetail.setCaptureRecordId(captureRecordId);
            orderDetail.setCommodityId(captureSuccessService.getById(captureRecordId).getCommodityId());
            orderDetailList.add(orderDetail);
            CaptureSuccess captureSuccess=new CaptureSuccess();
            captureSuccess.setId(captureRecordId);
            captureSuccess.setStatus(0);
            captureSuccessList.add(captureSuccess);
        }
        orderDetailService.saveBatch(orderDetailList);
        captureSuccessService.updateBatchById(captureSuccessList);
        return R.success("下单成功");
    }

    @Override
    public List<MailOrderDto> getOrderList(Integer status,Long userId) {
        return orderMapper.getOrderList(status,userId);
    }

    @Override
    public MailOrderDto getOrderDetailList(Long orderDetailId) {
        return orderMapper.getOrderDetailList(orderDetailId);
    }

    @Override
    public List<ExportOrderDto> getExportOrder() {
        return orderMapper.getExportOrder();
    }
}
