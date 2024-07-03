package com.doll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.doll.dto.LoginUser;
import com.doll.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.doll.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service("userUserDetailsService")
public class UserUserDetailsService implements UserDetailsService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,phone);
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        String permission=user.getRole();
        //根据用户查询权限信息，添加到LoginUser中
        List<String> permissionList=new ArrayList<>();
        permissionList.add(permission);
        return new LoginUser(user,permissionList);

//        String code = redisTemplate.opsForValue().get(phone);
//
//        if (code == null) {
//
//            throw new UsernameNotFoundException("Verification code not found or expired");
//        }
//        code=code.substring(code.length()-6);
//        return new org.springframework.security.core.userdetails.User(user.getPhone(), "123", AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
    }
}


