package com.doll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.doll.dto.LoginEmployee;
import com.doll.entity.Employee;
import com.doll.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service("adminUserDetailsService")
public class AdminUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,username);
        Employee admin =adminMapper.selectOne(queryWrapper);
        if (Objects.isNull(admin)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        String permission=admin.getRole();
        //根据用户查询权限信息，添加到LoginEmployee中
        List<String> permissionList=new ArrayList<>();
        permissionList.add(permission);
        return new LoginEmployee(admin,permissionList);
//        return new org.springframework.security.core.userdetails.User(admin.getUsername(), admin.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(admin.getRole()));
    }
}

