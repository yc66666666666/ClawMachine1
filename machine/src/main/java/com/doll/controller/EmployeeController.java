package com.doll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.doll.common.R;
import com.doll.entity.Employee;
import com.doll.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PasswordEncoder passwordEncoder;


//    @PostMapping("/login")
//    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
//        String password=employee.getPassword();
//        password= DigestUtils.md5DigestAsHex(password.getBytes());
//        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(Employee::getUsername,employee.getUsername());
//        Employee emp=employeeService.getOne(queryWrapper);
//        if (emp==null){
//            return  R.error("用户名或密码错误");
//        }
//        if (!emp.getPassword().equals(password)){
//            return R.error("用户名或密码错误");
//        }
//        if(emp.getStatus()==0){
//            return  R.error("账户已禁用");
//        }
//        request.getSession().setAttribute("employee",emp.getId());
//        return R.success(emp);
//    }



//    @PostMapping("/logout")
//    public  R<String> logout(HttpServletRequest request){
//        request.getSession().removeAttribute("employee");
//        return  R.success("退出成功");
//    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('superAdmin')")
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工,员工信息:{}",employee.toString());
        log.info("新增员工,员工信息:{}",employee.getIdNumber().substring(employee.getIdNumber().length()-8));

        employee.setPassword(passwordEncoder.encode(employee.getIdNumber()).substring(employee.getIdNumber().length()-8));
        employee.setRole("admin");
        //设置密码为身份证后八位
        employee.setPassword(DigestUtils.md5DigestAsHex(employee.getIdNumber().substring(employee.getIdNumber().length()-8).getBytes()));
        Long empId=(Long) request.getSession().getAttribute("employee");
        employeeService.save(employee);
        return  R.success("新增员工成功");
    }

    @GetMapping("/page")
    @PreAuthorize("hasAnyAuthority('superAdmin')")
    public  R<Page> pageR(int page,int pageSize,String name){
      log.info("page={},pageSize={},name={}",page,pageSize,name);
      Page<Employee> pageInfo=new Page(page,pageSize);
      LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper<>();
      queryWrapper.like(!StringUtils.isEmpty(name),Employee::getName,name);
      queryWrapper.orderByDesc(Employee::getUpdateTime);
      employeeService.page(pageInfo,queryWrapper);
      return R.success(pageInfo);
    }


    @PutMapping
    @PreAuthorize("hasAnyAuthority('superAdmin')")
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
        Long empId=(Long) request.getSession().getAttribute("employee");
        employeeService.updateById(employee);
        return  R.success("员工状态修改成功");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('superAdmin')")
    public R<Employee> getById(@PathVariable Long id){
       log.info("要修改的用户id为:{}",id);
       Employee employee=employeeService.getById(id);
       employee.setPassword(null);
       employee.setCreateUser(null);
       employee.setUpdateUser(null);
       return  R.success(employee);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('superAdmin')")
    public R<String> delete(Long id){
        employeeService.removeById(id);
        return R.success("删除管理员成功");
    }



}
