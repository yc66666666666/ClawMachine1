package com.doll.config;

import com.doll.common.AccessDeniedHandlerImpl;
import com.doll.common.AuthenticationEntryPointImpl;
import com.doll.filter.JwtRequestFilter;
import com.doll.service.impl.AdminUserDetailsService;
import com.doll.service.impl.CustomAuthenticationProvider;
import com.doll.service.impl.UserUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity//Spring项目需要添加此注释，SpringBoot项目中不需要
@EnableGlobalMethodSecurity(prePostEnabled = true)//开启基于方法的授权
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private AdminUserDetailsService adminUserDetailsService;
//
//    @Autowired
//    private UserUserDetailsService userUserDetailsService;


    @Autowired
    @Qualifier("adminUserDetailsService")
    private UserDetailsService adminUserDetailsService;

    @Autowired
    @Qualifier("userUserDetailsService")
    private UserDetailsService userUserDetailsService;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;


    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminUserDetailsService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//不通过Session获取Security
                .and()
                .authorizeRequests()
                .antMatchers("/employee/login").anonymous()
                .antMatchers("/user/loginWithCode").anonymous()
                .antMatchers("/user/sendMsg").anonymous()
                .anyRequest().authenticated();

        //添加过滤器
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        //配置认证失败处理器
        http.exceptionHandling().
                authenticationEntryPoint(authenticationEntryPoint).
                accessDeniedHandler(accessDeniedHandler);

        //允许跨域
        http.cors();


    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
