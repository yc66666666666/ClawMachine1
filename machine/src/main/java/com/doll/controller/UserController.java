package com.doll.controller;

import com.aliyuncs.utils.StringUtils;
import com.doll.common.BaseContext;
import com.doll.common.R;
import com.doll.dto.UserDto;
import com.doll.entity.User;
import com.doll.service.UserService;
import com.doll.utils.SMSUtils;
import com.doll.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone=user.getPhone();
        if(!StringUtils.isEmpty(phone)){
            String code= ValidateCodeUtils.generateValidateCode(6).toString();
            SMSUtils.sendMessage("瑞吉外卖","SMS_462260361",phone,code);
            session.setAttribute(phone,code);
            return R.success("手机验证码发送成功");
        }
        return R.error("短信发送失败");
    }

    @PutMapping("/bind")
    public R<String> bindPhone(@RequestBody UserDto user,HttpSession session){
        user.setId(BaseContext.getCurrentId());
        String phone=user.getPhone();
        String code=user.getCode();
        Object codeInSession = session.getAttribute(phone);
        if (codeInSession!=null && codeInSession.equals(code)){
            userService.updateById(user);
        }
        return R.success("绑定手机号码成功");
    }



    //登入功能没完成
    @PostMapping("/login")
    public R<User> login(){
        User user =new User();
        return R.success(user);
    }



}
