package com.guigui;

import cn.hutool.core.util.RandomUtil;
import com.doll.utils.PayUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import com.wechat.pay.contrib.apache.httpclient.util.RsaCryptoUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.bind.annotation.PostMapping;

import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

public class MyTest2 {

//    private CloseableHttpClient httpClient;
//    private AutoUpdateCertificatesVerifier verifier;
//
//
//    @Before
//    public void setup(){
//        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(PayUtils.privateKey.getBytes(StandardCharsets.UTF_8)));
//
//        //使用自动更新的签名验证器，不需要传入证书
//        verifier = new AutoUpdateCertificatesVerifier(
//                new WechatPay2Credentials(PayUtils.merchantId, new PrivateKeySigner(PayUtils.merchantSerialNumber , merchantPrivateKey)),
//               PayUtils.apiV3Key.getBytes(StandardCharsets.UTF_8));
//
//        httpClient = WechatPayHttpClientBuilder.create()
//                .withMerchant(PayUtils.merchantId, PayUtils.merchantSerialNumber, merchantPrivateKey)
//                .withValidator(new WechatPay2Validator(verifier))
//                .build();
//    }


    @Test
    public void createOrder() throws IOException, IllegalBlockSizeException {
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(PayUtils.privateKey.getBytes(StandardCharsets.UTF_8)));

        //使用自动更新的签名验证器，不需要传入证书
        AutoUpdateCertificatesVerifier  verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(PayUtils.merchantId, new PrivateKeySigner(PayUtils.merchantSerialNumber , merchantPrivateKey)),
                PayUtils.apiV3Key.getBytes(StandardCharsets.UTF_8));

        CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(PayUtils.merchantId, PayUtils.merchantSerialNumber, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier))
                .build();


        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
        httpPost.addHeader("Accept", "application/json");
        httpPost.addHeader("Content-type","application/json; charset=utf-8");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("mchid", PayUtils.merchantId)
                .put("appid", PayUtils.appId)
                .put("description", "Image形象店-深圳腾大-QQ公仔")
                .put("notify_url", PayUtils.notify_url)
                .put("out_trade_no", System.currentTimeMillis()+"111168");
        rootNode.putObject("amount")
                .put("total", 1);
        rootNode.putObject("payer")
                .put("openid", "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o");

        objectMapper.writeValue(bos, rootNode);

        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String bodyAsString = EntityUtils.toString(response.getEntity());
        System.out.println(bodyAsString);



//        //部分2
//        String timestamp = System.currentTimeMillis() + "";
//        String nonce = RandomUtil.randomString(32);
//        StringBuilder builder = new StringBuilder();
//        //应用id
//        builder.append(PayUtils.appId).append("\n");
//        //时间戳
//        builder.append(timestamp).append("\n");
//        //随机字符串
//        builder.append(nonce).append("\n");
//
//        JsonNode node = objectMapper.readTree(bodyAsString);
//        //预支付交易会话ID
//        builder.append(node.get("prepay_id")).append("\n");
//
//        String ciphertext = RsaCryptoUtil.encryptOAEP(builder.toString(), verifier.getValidCertificate());
//        System.out.println(ciphertext);
//        Map map=new HashMap();
//        map.put("noncesstr",nonce);
//        map.put("package",PayUtils.package1);
//        map.put("timestamp",timestamp);
//        map.put("sign",ciphertext);
//        System.out.println(map);
    }

//    @Test
//    public void  wef(){
//        // 创建一个空的 StringBuilder 对象
//        StringBuilder builder = new StringBuilder();
//
//        // 添加一些字符串
//        builder.append("Hello, ");
//        builder.append("world!");
//        builder.append(" This is an example of ");
//        builder.append("using StringBuilder.");
//
//        // 将 StringBuilder 转换为字符串并打印
//        String result = builder.toString();
//        System.out.println(result);
//
//    }




    @PostMapping("/callback")
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
            System.out.println(builder);

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
           System.out.println(PayUtils.decryptOrder(builder.toString()));

            //验证订单
            result.put("code","SUCCESS");
        }catch (IOException e){
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Test
    public void close() throws Exception {
        PayUtils.CloseOrder("214566");
    }
    @Test
    public void query() throws Exception {
        PayUtils.QueryOrder("u520vpayhcio3j5ht1563rmnnejkcuxp");
    }


}
