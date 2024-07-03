package com.doll.filter;

import com.alibaba.druid.util.StringUtils;
import com.doll.dto.LoginEmployee;
import com.doll.dto.LoginUser;
import com.doll.utils.JWTUtils;
import com.doll.utils.JwtUtil;
import com.doll.utils.PrefixUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("adminUserDetailsService")
    private UserDetailsService adminUserDetailsService;

    @Autowired
    @Qualifier("userUserDetailsService")
    private UserDetailsService userUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {



        //获取token
        String token = request.getHeader("JWTTOKEN");
        if (StringUtils.isEmpty(token)){
            chain.doFilter(request,response);
            return;
        }


        String role1 = token.substring(0, 1);
            // 获取剩余的部分
        String jwt = token.substring(1);


        String userId;
        //解析token
        try {
            Jws<Claims> claimsJws = JWTUtils.checkToken(jwt);
            userId= claimsJws.getBody().get("userId").toString();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }


        UsernamePasswordAuthenticationToken authenticationToken=null;
        //从redis中获取用户信息
        String redisKey = null;

        if ("a".equals(role1)){
            redisKey=PrefixUtils.SPRINGSECURITYADMIN+userId;
            LoginEmployee loginEmployee=(LoginEmployee) redisTemplate.opsForValue().get(redisKey);
            if (Objects.isNull(loginEmployee)){
                throw  new RuntimeException("用户未登入");
            }
            //TODO 获取权限信息封装到Authentication中
            authenticationToken= new UsernamePasswordAuthenticationToken(loginEmployee,null,loginEmployee.getAuthorities());

        } else if ("u".equals(role1)) {
            redisKey=PrefixUtils.SPRINGSECURITYUSER+userId ;
            LoginUser loginUser=(LoginUser) redisTemplate.opsForValue().get(redisKey);
            if (Objects.isNull(loginUser)){
                throw  new RuntimeException("用户未登入");
            }
            //TODO 获取权限信息封装到Authentication中
            authenticationToken= new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        }

//        LoginEmployee loginEmployee=(LoginEmployee) redisTemplate.opsForValue().get(redisKey);
//        if (Objects.isNull(loginEmployee)){
//            throw  new RuntimeException("用户未登入");
//        }

        //存入SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //放行
        chain.doFilter(request,response);


//        final String requestTokenHeader = request.getHeader("Authorization");
//
//
//        //获取token
//        String token = request.getHeader("token");
//        if (StringUtils.isEmpty(token)){
//            chain.doFilter(request,response);
//            return;
//        }
//
//
//
//        String username = null;
//        String jwtToken = null;
//
//        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//            jwtToken = requestTokenHeader.substring(7);
//            try {
//                username = jwtUtil.extractUsername(jwtToken);
//            } catch (IllegalArgumentException e) {
//                System.out.println("Unable to get JWT Token");
//            } catch (ExpiredJwtException e) {
//                System.out.println("JWT Token has expired");
//            }
//        } else {
//            logger.warn("JWT Token does not begin with Bearer String");
//        }
//
//        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails;
//            if (username.contains("@")) {  // Assuming username contains @ for admin
//                userDetails = adminUserDetailsService.loadUserByUsername(username);
//            } else {
//                userDetails = userUserDetailsService.loadUserByUsername(username);
//            }
//
//            if (jwtUtil.validateToken(jwtToken, userDetails)) {
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//            }
//        }
//        chain.doFilter(request, response);
    }
}

