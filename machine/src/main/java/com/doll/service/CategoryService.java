package com.doll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.doll.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);

}
