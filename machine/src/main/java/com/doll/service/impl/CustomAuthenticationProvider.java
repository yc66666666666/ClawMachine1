package com.doll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.doll.entity.User;
import com.doll.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

//    @Autowired
//    private UserUserDetailsService userUserDetailsService;

//    @Autowired
//    @Qualifier("userUserDetailsService")
//    private UserDetailsService userUserDetailsService;


    @Autowired
    @Qualifier("userUserDetailsService")
    private UserDetailsService userUserDetailsService;

    @Autowired
    private UserMapper userMapper;

//     @Autowired
//    private UserUserDetailsService userUserDetailsService ;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phone = authentication.getName();
        String code = (String) authentication.getCredentials();
        UserDetails userDetails = userUserDetailsService.loadUserByUsername(phone);
//        UserDetails userDetails = userUserDetailsService.loadUserByUsername(phone);
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(phone,code,AuthorityUtils.commaSeparatedStringToAuthorityList("123"));

//        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(User::getPhone,phone);
//        User user=userMapper.selectOne(queryWrapper);

        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found");
        }


        if (!code.equals(redisTemplate.opsForValue().get(phone))) {
            throw new BadCredentialsException("Invalid verification code");
        }
//        return (Authentication) userDetails;

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

