package com.doll.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.doll.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LoginEmployee implements UserDetails , Serializable {

    private static final long serialVersionUID = 1L;

    private Employee employee;

    private List<String> permissions ;

    public LoginEmployee(Employee employee, List<String> permissions) {
        this.employee = employee;
        this.permissions = permissions;
    }

    @JSONField(serialize = false)
    private  List<SimpleGrantedAuthority> authorities;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities !=null){
            return  authorities ;
        }
        //把permission中 String类型的权限信息封装成SimpleGrantedAuthority对象
        authorities=permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return authorities;
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return employee.getStatus() ==1 ? true :false;  //1为正常状态，其它为不正常状态
    }
}


