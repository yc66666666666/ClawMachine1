package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.doll.common.AuthenticationResponse;
import com.doll.common.R;
import com.doll.dto.*;
import com.doll.entity.User;
import com.doll.service.UserService;
import com.doll.service.impl.UserUserDetailsService;
import com.doll.utils.JWTUtils;
import com.doll.utils.JwtUtil;
import com.doll.utils.PrefixUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Qualifier("adminUserDetailsService")
    private UserDetailsService adminUserDetailsService;

    @Autowired
    @Qualifier("userUserDetailsService")
    private UserDetailsService userUserDetailsService;

    @Autowired
    private UserService userService;



    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/employee/login")
    public R<EmployeeReturnDto> createAdminAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        Authentication authentication= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
            if (Objects.isNull(authentication)){
                throw  new RuntimeException("登入失败");
            }
        //如果认证通过，使用一个userId生成一个jwt,jwt存入R返回
        LoginEmployee loginEmployee=(LoginEmployee) authentication.getPrincipal();
        Long userId=loginEmployee.getEmployee().getId();
        String jwt=JWTUtils.getToken(userId);
        jwt="a"+jwt;  //用于区分用户和管理员,管理员在jwt前面加a
        redisTemplate.opsForValue().set(PrefixUtils.SPRINGSECURITYADMIN+userId,loginEmployee);
        EmployeeReturnDto employeeReturnDto=new EmployeeReturnDto();
        employeeReturnDto.setJwt(jwt);
        employeeReturnDto.setUserId(userId);
        employeeReturnDto.setRole(loginEmployee.getEmployee().getRole());
        return R.success(employeeReturnDto);

//        final UserDetails userDetails = adminUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//        final String jwt = jwtUtil.generateToken(userDetails);
//        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/user/loginWithCode") //手机验证码登入
    public R<UserReturnDto> createUserAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        //判断是否是新用户
        LambdaQueryWrapper<User> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,authenticationRequest.getPhone());
        User user=userService.getOne(queryWrapper);
        if (Objects.isNull(user)){
            User user1=new User();
            user1.setPhone(authenticationRequest.getPhone());
            user1.setStatus(1);
            user1.setRegistrationTime(LocalDateTime.now());
            user1.setLatestLoginTime(LocalDateTime.now());
            user1.setRole("user");
            user1.setCoin(0);
            userService.save(user1);
        }



        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getPhone(), authenticationRequest.getCode()));
        if (Objects.isNull(authentication)){
            throw  new RuntimeException("登入失败");
        }
//        final   UserDetails userDetails = userUserDetailsService.loadUserByUsername(authenticationRequest.getPhone());
//////           UserDetails userDetails = null ;
////        User user=(User)authentication.getPrincipal();

        //如果认证通过，使用一个userId生成一个jwt,jwt存入R返回
        LoginUser loginUser=(LoginUser) authentication.getPrincipal();
        Long userId=loginUser.getUser().getId();
        String jwt=JWTUtils.getToken(userId);
        jwt="u"+jwt;  //用于区分用户和管理员，用户在jwt前面加u
        redisTemplate.opsForValue().set(PrefixUtils.SPRINGSECURITYUSER+userId,loginUser);
//        return ResponseEntity.ok(new AuthenticationResponse(jwt));

        //非新用户更新最新登入时间
        if (!Objects.isNull(user)){
            User user2=new User();
            user2.setId(userId);
            user2.setLatestLoginTime(LocalDateTime.now());
            userService.updateById(user2);
        }
//        redisTemplate.delete(authenticationRequest.getPhone()); //上线要加上

        UserReturnDto userReturnDto=new UserReturnDto();
        BeanUtils.copyProperties(loginUser.getUser(),userReturnDto);
        userReturnDto.setJwt(jwt);
        return R.success(userReturnDto);
    }

    @PostMapping("/employee/logout")
    public R<String> logoutAdmin(){
        UsernamePasswordAuthenticationToken authenticationToken= (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginEmployee loginEmployee=  (LoginEmployee) authenticationToken.getPrincipal();
        Long userid=loginEmployee.getEmployee().getId();
        //删除redis中的值
        redisTemplate.delete(PrefixUtils.SPRINGSECURITYADMIN+userid);
        return R.success("退出成功");
    }

    @PostMapping("/user/logout")
    public R<String> logoutUser(){
        UsernamePasswordAuthenticationToken authenticationToken=(UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser=(LoginUser) authenticationToken.getPrincipal();
        Long userId=loginUser.getUser().getId();
        //删除redis中的值
        redisTemplate.delete(PrefixUtils.SPRINGSECURITYUSER+userId);
        return R.success("退出成功");
    }




}
