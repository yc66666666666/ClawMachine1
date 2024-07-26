package com.doll.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.doll.common.R;
import com.doll.utils.PayUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

import static com.doll.utils.PayUtils.appId;


/**
 * @author Administrator
 * @version 1.0
 **/
@Slf4j
@Controller
public class WxPayController1 {


    //获取授权码
    @GetMapping("/getWXOAuth2Code")
    public void getWXOAuth2Code(HttpServletResponse response) throws IOException {
        System.out.println("getWXOAuth2Code--------------------");
        String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect",
                appId, PayUtils.wxOAuth2CodeReturnUrl);
        // 重定向到微信的授权URL
        response.sendRedirect(url);
    }


    /**
     * //授权码回调，传入授权码和state，/wx-oauth-code-return?code=授权码&state=
     *
     * @param code  授权码
     * @param state 申请授权码传入微信的值，被原样返回
     * @return
     */
    @GetMapping("/wx-oauth-code-return")
    public String wxOAuth2CodeReturn(@RequestParam String code, @RequestParam String state,HttpServletResponse response) throws IOException {
        System.out.println("wx-oauth-code-return-----------------");
        //https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                appId, PayUtils.appSecret , code
        );
        //申请openid，请求url
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        //申请openid接口响应的内容，其中包括了openid
        String body = exchange.getBody();
        System.out.println("返回的body:"+body);
        //获取openid
        String openid = JSON.parseObject(body).getString("openid");
        //重定向到统一下单接口
        String orderUrl="http://upbaby.qzhuanyou.com/wxjsapipayit?openid=" + openid;
        System.out.println("openid:"+openid);
//        response.sendRedirect(orderUrl);
        return openid ;
    }





        @GetMapping("/wxjsapipayit")
    public R createOrder(String openid,String money,String desc) throws Exception {
            //部分1，获得prepay_id
            System.out.println("wxjsapipayit-----------------");
            System.out.println("接受到的openid:" + openid);
            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(PayUtils.privateKey.getBytes(StandardCharsets.UTF_8)));
            //使用自动更新的签名验证器，不需要传入证书
            AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                    new WechatPay2Credentials(PayUtils.merchantId, new PrivateKeySigner(PayUtils.merchantSerialNumber, merchantPrivateKey)),
                    PayUtils.apiV3Key.getBytes(StandardCharsets.UTF_8));

            CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                    .withMerchant(PayUtils.merchantId, PayUtils.merchantSerialNumber, merchantPrivateKey)
                    .withValidator(new WechatPay2Validator(verifier))
                    .build();


            //请求URL
            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
            String out_trade_no=RandomUtil.randomString(32);
            // 请求body参数
            String reqdata = String.format("{"
                    + "\"amount\": {"
                    + "\"total\": %s,"
                    + "\"currency\": \"CNY\""
                    + "},"
                    + "\"mchid\": \"%s\","
                    + "\"description\": \"Image形象店-深圳腾大-QQ公仔\","
                    + "\"notify_url\": \"%s\","
                    + "\"payer\": {"
                    + "\"openid\": \"%s\"" + "},"
                    + "\"out_trade_no\": \"%s\","
                    + "\"goods_tag\": \"WXG\","
                    + "\"appid\": \"%s\"" + "}",money,PayUtils.merchantId,PayUtils.notify_url,openid,out_trade_no, appId);
            StringEntity entity = new StringEntity(reqdata,"utf-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");

            //完成签名并执行请求
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String bodyAsString=EntityUtils.toString(response.getEntity());
            String prepayId=null;
            try {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    System.out.println("success,return body = " + EntityUtils.toString(response.getEntity()));
                    JsonObject jsonObject = new Gson().fromJson(bodyAsString, JsonObject.class);
                    //获取微信支付中的预支付id
                    prepayId = jsonObject.get("prepay_id").getAsString();

                } else if (statusCode == 204) {
                    System.out.println("success");
                } else {
                    System.out.println("failed,resp code = " + statusCode+ ",return body = " + EntityUtils.toString(response.getEntity()));
                    throw new IOException("request failed");
                }
            } finally {
                response.close();
                httpClient.close();
            }


//            String privateKeyPath = "apiclient_key.pem"; // 替换为你的私钥文件路径
//            PrivateKey privateKey = getPrivateKey(privateKeyPath);

            String appId = PayUtils.appId;
            String timeStamp = System.currentTimeMillis() / 1000 + "";
            String nonceStr = RandomUtil.randomString(32);

            String sign = createSign(appId, timeStamp, nonceStr, prepayId, merchantPrivateKey);
            System.out.println("Generated Sign: " + sign);




        //准备h5网页需要的数据
        Map<String, String> jsapiPayParam = new HashMap<>();
        jsapiPayParam.put("appId", appId);
        jsapiPayParam.put("timeStamp", timeStamp);
        jsapiPayParam.put("nonceStr", nonceStr);//随机字符串
        jsapiPayParam.put("package", "prepay_id="+prepayId);
//        jsapiPayParam.put("signType", "HMAC-SHA256");
//        String paySign=WXPayUtil.generateSignature(jsapiPayParam, PayUtils.apiV3Key, WXPayConstants.SignType.HMACSHA256);
        jsapiPayParam.put("signType", "RSA");
        //将h5网页响应给前端
//        jsapiPayParam.put("paySign", this.sign(jsapiPayParam.toString().getBytes()));
        jsapiPayParam.put("paySign",sign);
        System.out.println("1123:"+jsapiPayParam.toString());
          return   R.success(jsapiPayParam);

        }

    // 读取私钥
    public static PrivateKey getPrivateKey(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
        String privateKeyPEM = new String(keyBytes);
        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        keyBytes = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    // 生成签名
    public static String createSign(String appId, String timeStamp, String nonceStr, String prepayId, PrivateKey privateKey) throws Exception {
        // 拼接字符串
        String signStr = appId + "\n" +
                timeStamp + "\n" +
                nonceStr + "\n" +
                "prepay_id=" + prepayId + "\n";

        System.out.println(signStr);

        // 使用RSA进行签名
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(signStr.getBytes("UTF-8"));

        // 生成签名并进行Base64编码
        byte[] signedData = signature.sign();
        return Base64.getEncoder().encodeToString(signedData);
    }










//    public static PrivateKey getPrivateKey(String filename) throws Exception {
//        InputStream is = WxPayController1.class.getClassLoader().getResourceAsStream(filename);
//        if (is == null) {
//            throw new IllegalArgumentException("File not found: " + filename);
//        }
//
//        byte[] keyBytes = toByteArray(is);
//        String privateKeyPEM = new String(keyBytes);
//        privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "")
//                .replace("-----END PRIVATE KEY-----", "")
//                .replaceAll("\\s", "");
//        keyBytes = Base64.getDecoder().decode(privateKeyPEM);
//        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        return keyFactory.generatePrivate(spec);
//    }
//
//    private static byte[] toByteArray(InputStream is) throws Exception {
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        int nRead;
//        byte[] data = new byte[1024];
//        while ((nRead = is.read(data, 0, data.length)) != -1) {
//            buffer.write(data, 0, nRead);
//        }
//        buffer.flush();
//        return buffer.toByteArray();
//    }
//
//    public static String signData(String data, PrivateKey privateKey) throws Exception {
//        Signature signature = Signature.getInstance("SHA256withRSA");
//        signature.initSign(privateKey);
//        signature.update(data.getBytes("UTF-8"));
//        byte[] signedData = signature.sign();
//        return Base64.getEncoder().encodeToString(signedData);
//    }




    @PostMapping("/wxcallback") //支付成功回调
    public Map callback(HttpServletRequest request){

        String Timestamp=request.getHeader("Wechatpay-Timestamp");
        String Nonce=request.getHeader("Wechatpay-Nonce");
        String signature=request.getHeader("Wechatpay-Signature");
        String serialNumber=request.getHeader("Wechatpay-Serial");
        System.out.println("Wechatpay-Timestamp:"+request.getHeader("Wechatpay-Timestamp"));
        System.out.println("Wechatpay-Nonce:"+request.getHeader("Wechatpay-Nonce"));
        System.out.println("Wechatpay-Signature:"+request.getHeader("Wechatpay-Signature"));
        System.out.println("Wechatpay-Serial:"+request.getHeader("Wechatpay-Serial"));

        Map result=new HashMap();
        result.put("code","FAIL");
        try {
            BufferedReader bufferedReader = request.getReader(); //获得请求体
            String str=null;
            StringBuilder builder=new StringBuilder();
            while ((str=bufferedReader.readLine())!=null){
                builder.append(builder);
            }
            System.out.println("builder::"+builder);

            //验证签名
            StringBuilder signStr=new StringBuilder();
            signStr.append(Timestamp).append("\n");
            signStr.append(Nonce).append("\n");
            signStr.append(builder.toString()).append("\n");
            if (!PayUtils.signVerifier(serialNumber,signStr.toString(),signature)){
                result.put("message","sign error");
                return result ;
            }
            //解密密文
            System.out.println("被解密的密文：："+PayUtils.decryptOrder(builder.toString()));
            //验证订单
            result.put("code","SUCCESS");
        }catch (IOException e){
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        return result;
    }



}
