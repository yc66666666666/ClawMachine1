package com.doll.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

public class JWTUtils {

    private static long  time = 1000*60*60*24;

    private static String signature = "admin618" ;



    public static String getToken(Long userId){
        JwtBuilder jwtBuilder= Jwts.builder();
        String jwtToken =jwtBuilder
                //header
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                //payload
                .claim("username","tom")
                .claim("userId",userId.toString())
                .claim("role","admin")
                .setSubject("admin-test")
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .setId(UUID.randomUUID().toString())    //设置JWT的唯一标识符
                //signature
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();
        return  jwtToken;
    }

    public static Jws<Claims> checkToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signature).parseClaimsJws(token);
        return claimsJws ;
    }


}

