package com.doll.controller;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.doll.common.BaseContext;
import com.doll.common.R;
import com.doll.dto.UserDto;
import com.doll.entity.User;
import com.doll.service.UserService;
import com.doll.utils.SMSUtils;
import com.doll.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")    //发送验证码
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone=user.getPhone();
        if(!StringUtils.isEmpty(phone)){
            String code= ValidateCodeUtils.generateValidateCode(6).toString();
            SMSUtils.sendMessage("泉州寰游科技","SMS_300165018",phone,code);
//            session.setAttribute(phone,code);   //把验证码存放在session里
            redisTemplate.opsForValue().set(phone,code,60, TimeUnit.MINUTES);  //把验证码存放在redis里，有效期为5分钟
            return R.success("手机验证码发送成功");
        }
        return R.error("短信发送失败");
    }

    @PutMapping("/bind")   //绑定手机
    public R<String> bindPhone(@RequestBody UserDto user,HttpSession session){
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone,user.getPhone());
        int count=userService.count(queryWrapper);
        if (count>0){                           //判断该手机是否被绑定
            return R.error("该手机已经绑定了账户");
        }

        user.setId(BaseContext.getCurrentId()); //实际用
//        user.setId(1417012167126876162L); //测试用
        String phone=user.getPhone();
        String code=user.getCode();
//        Object codeInSession = session.getAttribute(phone);   //从session中获取验证码
        Object codeInSession = redisTemplate.opsForValue().get(phone);//从redis中获取验证码
        if (codeInSession!=null && codeInSession.equals(code)){
            userService.updateById(user);
        }
        redisTemplate.delete(phone); //删除redis里面的缓存
        return R.success("绑定手机号码成功");
    }


    @PostMapping("/loginWithCode") //通过手机验证码登入
    public R<User> login(@RequestBody Map map,HttpSession session){
         String phone =map.get("phone").toString();
         String code=map.get("code").toString();
         Object codeInRedis=redisTemplate.opsForValue().get(phone);
         if (codeInRedis !=null && codeInRedis.equals(code)){
             LambdaQueryWrapper<User> queryWrapper =new LambdaQueryWrapper<>();
             queryWrapper.eq(User::getPhone,phone);
             User user=userService.getOne(queryWrapper);
             if (user==null){
                 user=new User();
                 user.setPhone(phone);
                 user.setStatus(1);
                 user.setRegistrationTime(LocalDateTime.now());
                 user.setLatestLoginTime(LocalDateTime.now());
                 userService.save(user);
             }
             session.setAttribute("user",user.getId());
             User user1=new User();
             user1.setId(user.getId());
             user1.setLatestLoginTime(LocalDateTime.now());
             userService.updateById(user1);
//             redisTemplate.delete(phone);     //上线要加上
             user.setLatestLoginTime(LocalDateTime.now());
             return R.success(user);
         }
         return R.error("登陆失败");
    }







}
