package com.doll.controller;

import com.doll.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${doll.path}")
    private String basePath;
    @PostMapping("/upload")   //将文件上传到服务器
    public R<String> upload(MultipartFile file){
        log.info(file.toString());
        String originalFileName=file.getOriginalFilename();
        String suffix=originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName= UUID.randomUUID().toString()+suffix;
        File dir=new File(basePath);
        if (!dir.exists()){
            dir.mkdir();
        }
        try {
            file.transferTo(new File(basePath+fileName));
        }catch (IOException e){
            e.printStackTrace();
        }
        return R.success(fileName);
    }
    @GetMapping("/download")   //将文件从服务器下载到本地
    public void download(String name, HttpServletResponse response){
        try{
            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));
            ServletOutputStream outputStream=response.getOutputStream();
            response.setContentType("image/jpeg");
            int len=0;
            byte[] bytes=new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
