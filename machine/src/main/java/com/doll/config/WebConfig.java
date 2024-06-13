package com.doll.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 对所有的URL都有效
                .allowedOrigins("*")  // 允许域,*代表所有，以后可以改，限制范围
                .allowedMethods("GET", "POST", "PUT", "DELETE","HEAD")  // 允许方法
                .allowedHeaders("*")  // 允许头
                .allowCredentials(true)  // 允许证书
                .maxAge(3600);  // 预检请求的缓存时间（秒）
    }
}
