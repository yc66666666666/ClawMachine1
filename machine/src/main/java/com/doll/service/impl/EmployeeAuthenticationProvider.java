package com.doll.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.doll.entity.User;
import com.doll.mapper.UserMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("employeeAuthentication")
public class EmployeeAuthenticationProvider implements AuthenticationProvider {
    //管理员自定义验证方法

//    @Autowired
//    private UserUserDetailsService userUserDetailsService;

//    @Autowired
//    @Qualifier("userUserDetailsService")
//    private UserDetailsService userUserDetailsService;


    @Autowired
    @Qualifier("adminUserDetailsService")
    private UserDetailsService adminUserDetailsService;


    @Autowired
    private PasswordEncoder passwordEncoder ;



    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password=(String) authentication.getCredentials();
        UserDetails userDetails = adminUserDetailsService.loadUserByUsername(username);
//        UserDetails userDetails = userUserDetailsService.loadUserByUsername(phone);
//        UserDetails userDetails = new org.springframework.security.core.userdetails.User(phone,code,AuthorityUtils.commaSeparatedStringToAuthorityList("123"));


        boolean isSuccess=passwordEncoder.matches(password,userDetails.getPassword()) ;
        if (!isSuccess){
            throw new UsernameNotFoundException("User not found");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
