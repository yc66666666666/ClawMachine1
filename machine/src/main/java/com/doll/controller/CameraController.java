package com.doll.controller;

import com.doll.common.R;
import com.doll.dto.CameraResponse;
import com.doll.entity.Component;
import com.doll.service.CameraService;
import com.doll.service.ComponentService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping("/camera")
public class CameraController {

    @Autowired
    private CameraService cameraService;

    @Autowired
    private ComponentService componentService;

    @Value("${camera.APP_KEY}")
    private  String APP_KEY ;
    @Value("${camera.APP_SECRET}")
    private  String APP_SECRET;
    @Value("${camera.ACCESS_TOKEN_URL}")
    private  String ACCESS_TOKEN_URL;
    @Value("${camera.VIDEO_URL}")
    private  String VIDEO_URL;

    @GetMapping("/getWechatVideoUrlAndAccessTokenById/{cameraId}")
    public R<CameraResponse> getWeChatVideoUrlAndAccessTokenById(@PathVariable Long cameraId,String videoType ){
          Component camera=componentService.getById(cameraId);
           return cameraService.getVideoUrlAndAccessToken("weiXin",camera.getName(),camera.getChannelNo(),videoType,null,null);
    }

    @GetMapping("/getH5VideoUrlAndAccessTokenById/{cameraId}")
    public R<CameraResponse> getH5VideoUrlAndAccessTokenById(@PathVariable Long cameraId ){
        Component camera=componentService.getById(cameraId);
        return cameraService.getVideoUrlAndAccessToken("H5",camera.getName(),camera.getChannelNo(),null,null,null);
    }





//    @GetMapping("/ezviz/token")
//    public String getAccessToken() {
//        // 设置 RestTemplate 和 HTTP Headers
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        // 构造 POST 请求的主体数据
//        String body = String.format("appKey=%s&appSecret=%s", APP_KEY, APP_SECRET);
//        HttpEntity<String> entity = new HttpEntity<>(body, headers);
//
//        // 发送 POST 请求
//        ResponseEntity<String> response = restTemplate.exchange(
//                ACCESS_TOKEN_URL,
//                HttpMethod.POST,
//                entity,
//                String.class
//        );
//
//
//
//        // 解析 JSON 字符串
//        JsonObject rootObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
//
//        // 获取 data 节点
//        JsonObject dataObject = rootObject.getAsJsonObject("data");
//
//        // 获取 accessToken 的值
//        String accessToken = dataObject.get("accessToken").getAsString();
//
//        // 输出 accessToken
//        System.out.println("Access Token: " + accessToken);
//
//
//        return response.getBody();
//    }
//

    /**
     * 获取摄像头实时直播地址和accessToken
     */


    @GetMapping("/ezviz/video")
    public R<CameraResponse> getVideoUrlAndAccessToken(@RequestParam String ApplicationType,@RequestParam String deviceSerial, @RequestParam int channelNo, @RequestParam(required = false) String videoType,@RequestParam(required = false) String startTime,@RequestParam(required = false)String endTime) {

        RestTemplate restTemplate1 = new RestTemplate();
        HttpHeaders headers1 = new HttpHeaders();
        headers1.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 构造 POST 请求的主体数据
        String body1 = String.format("appKey=%s&appSecret=%s", APP_KEY, APP_SECRET);
        HttpEntity<String> entity1 = new HttpEntity<>(body1, headers1);
        // 发送 POST 请求
        ResponseEntity<String> response1 = restTemplate1.exchange(
                ACCESS_TOKEN_URL,
                HttpMethod.POST,
                entity1,
                String.class
        );
        // 解析 JSON 字符串
        JsonObject rootObject = JsonParser.parseString(response1.getBody()).getAsJsonObject();
        // 获取 data 节点
        JsonObject dataObject = rootObject.getAsJsonObject("data");
        String accessTokenMSG=rootObject.get("msg").getAsString();
        // 获取 accessToken 的值
        String accessToken = dataObject.get("accessToken").getAsString();
        if (accessToken == null) {
            return R.error(accessTokenMSG);
        }

         String url=null;    //视频链接
        //如果是微信url要变
        if (ApplicationType.equals("weiXin")) {
            // 定义日期和时间的格式
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            // 使用 formatter 将 startTime 格式化为 String
//            String StartDateTime = startTime.format(formatter);
//            // 使用 formatter 将 endTime 格式化为 String
//            String EndDateTime = endTime.format(formatter);
            url = String.format("rtmp://open.ys7.com/%s/%d/%s/%s/%s", deviceSerial, channelNo, videoType, startTime, endTime);
        }

        if(ApplicationType.equals("H5")) {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String body = String.format("accessToken=%s&deviceSerial=%s&channelNo=%d", accessToken, deviceSerial, channelNo);
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(VIDEO_URL, HttpMethod.POST, entity, String.class);
            // 解析 JSON 字符串
            JsonObject rootObject2 = JsonParser.parseString(response.getBody()).getAsJsonObject();
            // 获取 data 节点
            JsonObject dataObject2 = rootObject2.getAsJsonObject("data");
            String urlMSG = rootObject2.get("msg").getAsString();
            // 获取 url 的值，H5的url
            url = dataObject2.get("url").getAsString();

            if (url == null) {
                return R.error(urlMSG);
            }
        }

        CameraResponse cameraResponse=new CameraResponse();
        cameraResponse.setAccessToken(accessToken);
        cameraResponse.setUrl(url);
        return R.success(cameraResponse);
    }
}

