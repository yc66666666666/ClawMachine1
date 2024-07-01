package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doll.common.R;
import com.doll.dto.MailOrderDto;
import com.doll.dto.OrderDto;
import com.doll.entity.AddressBook;
import com.doll.entity.CaptureSuccess;
import com.doll.entity.MailOrder;
import com.doll.entity.OrderDetail;
import com.doll.service.AddressBookService;
import com.doll.service.CaptureSuccessService;
import com.doll.service.OrderDetailService;
import com.doll.service.OrderService;
import com.doll.utils.ExportFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService ;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private CaptureSuccessService captureSuccessService;

    @Autowired
    private OrderDetailService orderDetailService;


    @PostMapping("/saveOrderAndDetail")
    public R<String> saveOrderAndDetail(@RequestBody OrderDto orderDto){
    return orderService.saveOrderAndDetail(orderDto);
    }


    @GetMapping("/getOrderList")
    public  R<List<MailOrderDto>>  getOrderList(Integer status,Long userId){
        return R.success(orderService.getOrderList(status,userId));
    }
    @GetMapping("/getOrderDetailById")
    public R<MailOrderDto> getOrderDetail(Long orderDetailId,Long mailOrderAddressBookId){
        MailOrderDto mailOrderDto=orderService.getOrderDetailList(orderDetailId);
        AddressBook addressBook=addressBookService.getById(mailOrderAddressBookId);
        mailOrderDto.setAddressBook(addressBook);
        return R.success(mailOrderDto);
    }

    @PutMapping("/cancelOrder")
    @Transactional
    public R<String> cancelOrder(Long orderDetailId){
        OrderDetail orderDetail=orderDetailService.getById(orderDetailId);
        CaptureSuccess captureSuccess=new CaptureSuccess();
        captureSuccess.setId(orderDetail.getCaptureRecordId());
        captureSuccess.setStatus(1);
        captureSuccessService.updateById(captureSuccess);
        LambdaQueryWrapper<OrderDetail> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId,orderDetail.getOrderId());
        int count=orderDetailService.count(queryWrapper);
        if (count==1){
            orderService.removeById(orderDetail.getOrderId());
        }
        orderDetailService.removeById(orderDetailId);
        return R.success("取消订单成功");
    }


    @GetMapping("/export")
    public String exportAddressBook(String filePath ) {
//        String filePath = "D:/img1/orders.xlsx";
            filePath=filePath+".xlsx";
        try {
            ExportFileUtils.writeAddressBookToExcel( orderService.getExportOrder(),filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "file created: " + filePath;
    }

    @GetMapping("/getOrders")
    public R<Page<MailOrder>> getOrders(Integer page,Integer pageSize){
        Page<MailOrder> page1=new Page<>(page,pageSize);
        LambdaQueryWrapper<MailOrder> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(MailOrder::getStatus,0);
        return R.success(orderService.page(page1,queryWrapper));
    }
    @PutMapping("/addCourierNumberAndBrand")
    public R<String> addCourierNumberAndBrand(Long orderId,String courierNumberAndBrand){
        MailOrder mailOrder=new MailOrder();
        mailOrder.setId(orderId);
        mailOrder.setStatus(1);
        mailOrder.setCourierNumber(courierNumberAndBrand);
        orderService.updateById(mailOrder);
        return R.success("添加成功");
    }




}
