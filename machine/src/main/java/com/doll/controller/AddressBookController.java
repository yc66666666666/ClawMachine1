package com.doll.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.doll.common.BaseContext;
import com.doll.common.R;
import com.doll.entity.AddressBook;
import com.doll.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    //添加地址
    @PostMapping
    @Transactional
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        Integer isDefault=addressBook.getIsDefault();
        if (isDefault==1){
            LambdaUpdateWrapper<AddressBook> updateWrapper=new LambdaUpdateWrapper<>();
             updateWrapper.set(AddressBook::getIsDefault,0).eq(AddressBook::getUserId,BaseContext.getCurrentId());
            addressBookService.update(updateWrapper);
        }
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }


    @PutMapping("/default")//传过来AddressBook的id
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
         AddressBook addressBook1= addressBookService.setDefault(addressBook);
        return R.success(addressBook1);
    }

    @GetMapping("/{id}")
    public R get(@PathVariable Long id){
        AddressBook addressBook=addressBookService.getById(id);
        if(addressBook!=null){
            return R.success(addressBook);
        }else {
            return R.error("没有对应的地址");
        }
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        LambdaQueryWrapper<AddressBook> queryWrapper=new LambdaQueryWrapper<>();
        System.out.println(BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook=addressBookService.getOne(queryWrapper);
        if (addressBook==null){
            return R.error("没找到");
        }else {
            return R.success(addressBook);
        }

    }

    @GetMapping("/list")   //传用户id
     public R<List<AddressBook>> listR(AddressBook addressBook){
        LambdaQueryWrapper<AddressBook> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        return R.success(addressBookService.list(queryWrapper));
     }

     @PutMapping
     public R<String> update(@RequestBody AddressBook addressBook){
         Integer isDefault=addressBook.getIsDefault();
         if (isDefault==1){
             LambdaUpdateWrapper<AddressBook> updateWrapper=new LambdaUpdateWrapper<>();
             updateWrapper.set(AddressBook::getIsDefault,0).eq(AddressBook::getUserId,BaseContext.getCurrentId());
             addressBookService.update(updateWrapper);
         }
        addressBookService.updateById(addressBook);
        return R.success("修改地址成功");
     }


    @DeleteMapping
    public R<String> delete(String id){
        addressBookService.removeById(id);
        return R.success("删除地址成功");
    }





}
