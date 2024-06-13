package com.doll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.doll.common.BaseContext;
import com.doll.entity.AddressBook;
import com.doll.mapper.AddressBookMapper;
import com.doll.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Override
    @Transactional
    public AddressBook setDefault(AddressBook addressBook) {

        Long id=addressBook.getId();
        LambdaQueryWrapper<AddressBook> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, addressBook.getUserId());
        addressBook.setIsDefault(0);
        addressBook.setId(null);
        addressBook.setUserId(null);
        super.update(addressBook,queryWrapper);
        addressBook.setIsDefault(1);
        addressBook.setId(id);
        super.updateById(addressBook);
        return  addressBook;
    }
}
