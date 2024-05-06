package com.doll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doll.entity.AddressBook;


public interface AddressBookService extends IService<AddressBook> {
    public AddressBook setDefault(AddressBook addressBook);
}
