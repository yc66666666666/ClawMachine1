//package com.doll.controller;
//
//import cn.hutool.core.util.RandomUtil;
//import com.alibaba.fastjson.JSON;
//import com.doll.utils.ClientIPUtil;
//import com.doll.utils.PayUtils;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
//import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
//import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
//import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
//import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
//import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
//import com.wechat.pay.contrib.apache.httpclient.util.RsaCryptoUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.junit.Test;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.servlet.ModelAndView;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
////import okhttp3.HttpUrl;
//import java.security.*;
//import java.util.Base64;
//
//import javax.crypto.IllegalBlockSizeException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//
//
//
//
//
//
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.security.KeyFactory;
//import java.security.PrivateKey;
//import java.security.Signature;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.util.Base64;
//
//
//
//
//
///**
// * @author Administrator
// * @version 1.0
// **/
//@Slf4j
//@Controller
//public class WxPayController {
//
//
//    //获取授权码
//    @GetMapping("/getWXOAuth2Code")
//    public void getWXOAuth2Code(HttpServletResponse response) throws IOException {
//        System.out.println("getWXOAuth2Code--------------------");
//        String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect",
//                PayUtils.appId, PayUtils.wxOAuth2CodeReturnUrl);
//        // 重定向到微信的授权URL
//        response.sendRedirect(url);
//    }
//
//
//    /**
//     * //授权码回调，传入授权码和state，/wx-oauth-code-return?code=授权码&state=
//     *
//     * @param code  授权码
//     * @param state 申请授权码传入微信的值，被原样返回
//     * @return
//     */
//    @GetMapping("/wx-oauth-code-return")
//    public void wxOAuth2CodeReturn(@RequestParam String code, @RequestParam String state,HttpServletResponse response) throws IOException {
//        System.out.println("wx-oauth-code-return-----------------");
//        //https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
//        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
//                PayUtils.appId, PayUtils.appSecret , code
//        );
//        //申请openid，请求url
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
//        //申请openid接口响应的内容，其中包括了openid
//        String body = exchange.getBody();
//        System.out.println("返回的body:"+body);
//        //获取openid
//        String openid = JSON.parseObject(body).getString("openid");
//        //重定向到统一下单接口
//        String orderUrl="http://upbaby.qzhuanyou.com/wxjsapipayit?openid=" + openid;
//        System.out.println("openid:"+openid);
//        response.sendRedirect(orderUrl);
//    }
//
//
//
////    @GetMapping("/wxjsapipayit")
////    public ModelAndView wxjspay(HttpServletRequest request, HttpServletResponse response) throws Exception {
////        System.out.println("wxjspay-----------------");
////        //创建sdk客户端
////        WXPay wxPay = new WXPay(new WXPayConfigCustom());
////        //构造请求的参数
////        Map<String, String> requestParam = new HashMap<>();
////        requestParam.put("out_trade_no", System.currentTimeMillis()+"");//订单号
////        requestParam.put("body", "iphone8");//订单描述
////        requestParam.put("fee_type", "CNY");//人民币
////        requestParam.put("total_fee", String.valueOf(1)); //金额
////        requestParam.put("spbill_create_ip", ClientIPUtil.getClientIp(request));//客户端ip
////        requestParam.put("notify_url", "none");//微信异步通知支付结果接口，暂时不用
////        requestParam.put("trade_type", "JSAPI");
////        //从请求中获取openid
////        String openid = request.getParameter("openid");
////        requestParam.put("openid", openid);
////        //调用统一下单接口
////        Map<String, String> resp = wxPay.unifiedOrder(requestParam);
////        System.out.println("客户端ip:"+ClientIPUtil.getClientIp(request));
////        System.out.println("预支付prepay_id:"+resp.get("prepay_id"));
////        //准备h5网页需要的数据
////        Map<String, String> jsapiPayParam = new HashMap<>();
////        jsapiPayParam.put("appId", PayUtils.appId);
////        jsapiPayParam.put("timeStamp", System.currentTimeMillis() / 1000 + "");
////        jsapiPayParam.put("nonceStr", UUID.randomUUID().toString());//随机字符串
////        jsapiPayParam.put("package", "prepay_id=" + resp.get("prepay_id"));
////        jsapiPayParam.put("signType", "HMAC-SHA256");
////        //将h5网页响应给前端
////        jsapiPayParam.put("paySign", WXPayUtil.generateSignature(jsapiPayParam, PayUtils.apiV3Key, WXPayConstants.SignType.HMACSHA256));
////
////        return new ModelAndView("wxpay", jsapiPayParam);
////    }
////    class WXPayConfigCustom extends WXPayConfig {
////
////        @Override
////        protected String getAppID() {
////            return PayUtils.appId;
////        }
////
////        @Override
////        protected String getMchID() {
////            return PayUtils.merchantId;
////        }
////
////        @Override
////        protected String getKey() {
////            return PayUtils.apiV3Key;
////        }
////
////        @Override
////        protected InputStream getCertStream() {
////            return null;
////        }
////
////        @Override
////        protected IWXPayDomain getWXPayDomain() {
////            return new IWXPayDomain() {
////                @Override
////                public void report(String s, long l, Exception e) {
////
////                }
////
////                @Override
////                public DomainInfo getDomain(WXPayConfig wxPayConfig) {
////                    return new DomainInfo(WXPayConstants.DOMAIN_API, true);
////              }
////           };
////        }
////    }
//
//
//        @GetMapping("/wxjsapipayit")
//    public ModelAndView createOrder(HttpServletRequest request) throws Exception {
//            //部分1，获得prepay_id
//            System.out.println("wxjsapipayit-----------------");
//            String openid = request.getParameter("openid");
//            System.out.println("接受到的openid:" + openid);
//            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(PayUtils.privateKey.getBytes(StandardCharsets.UTF_8)));
//            //使用自动更新的签名验证器，不需要传入证书
//            AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
//                    new WechatPay2Credentials(PayUtils.merchantId, new PrivateKeySigner(PayUtils.merchantSerialNumber, merchantPrivateKey)),
//                    PayUtils.apiV3Key.getBytes(StandardCharsets.UTF_8));
//
//            CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
//                    .withMerchant(PayUtils.merchantId, PayUtils.merchantSerialNumber, merchantPrivateKey)
//                    .withValidator(new WechatPay2Validator(verifier))
//                    .build();
//
//            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
//            httpPost.addHeader("Accept", "application/json");
//            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
//
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            ObjectMapper objectMapper = new ObjectMapper();
//            String out_trade_no1=RandomUtil.randomString(32);
//            System.out.println("out_trade_no1::"+out_trade_no1);
//            ObjectNode rootNode = objectMapper.createObjectNode();
//            rootNode.put("mchid", PayUtils.merchantId)
//                    .put("appid", PayUtils.appId)
//                    .put("description", "Image形象店-深圳腾大-QQ公仔")
//                    .put("notify_url", PayUtils.notify_url)
//                    .put("out_trade_no",out_trade_no1 );
//            rootNode.putObject("amount")
//                    .put("total", 10);
//            rootNode.putObject("payer")
//                    .put("openid", openid);
//
//            objectMapper.writeValue(bos, rootNode);
//
//            httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
//            CloseableHttpResponse response = httpClient.execute(httpPost);
//
//            String bodyAsString = EntityUtils.toString(response.getEntity());
//            System.out.println("bodyAsString::::" + bodyAsString);
//
//
//
//
//
//
//
//        //部分2,获得签名
//        String timestamp = System.currentTimeMillis() / 1000 + "";
//        String nonce = RandomUtil.randomString(32);
//        StringBuilder builder = new StringBuilder();
//        //应用id
//        builder.append(PayUtils.appId).append("\n");
//        //时间戳
//        builder.append(timestamp).append("\n");
//        //随机字符串
//        builder.append(nonce).append("\n");
////
//        JsonNode node = objectMapper.readTree(bodyAsString);
//        String prepay_id=node.get("prepay_id").asText();
//        //预支付交易会话ID
//        builder.append(prepay_id).append("\n");
//
////        System.out.println("预支付id:::："+node.get("prepay_id"));
//
////        String ciphertext = RsaCryptoUtil.encryptOAEP(builder.toString(), verifier.getValidCertificate());
////        System.out.println("ciphertext::"+ciphertext);
////        Map map=new HashMap();
////        map.put("noncesstr",nonce);
////        map.put("package",PayUtils.package1);
////        map.put("timestamp",timestamp);
////        map.put("sign",ciphertext);
////        System.out.println("map:::::"+map);
//
//
//            String paySign = null;
//
//            try {
//                String privateKeyPath = "apiclient_key.pem"; // resources下的文件路径
//                PrivateKey privateKey = getPrivateKey(privateKeyPath);
////
////                String data= String.format("%s\n%s\n%s\nprepay_id=%s\n",PayUtils.appId,timestamp,nonce,prepay_id);
//                System.out.println("PayUtils.appId:"+PayUtils.appId);
//                System.out.println("timestamp:"+timestamp);
//                System.out.println("nonce:"+nonce);
//                System.out.println("prepay_id:"+prepay_id);
//
//                System.out.println("data:"+builder.toString());
////                paySign = signData(data, privateKey);
//
//
//
//
//                Signature sign = Signature.getInstance("SHA256withRSA");
//                sign.initSign(privateKey);
//                sign.update(builder.toString().getBytes());
//                paySign= Base64.getEncoder().encodeToString(sign.sign());
//
//                System.out.println("Signed Data: " + paySign);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//

//
//
//
//        //准备h5网页需要的数据
//        Map<String, String> jsapiPayParam = new HashMap<>();
//        jsapiPayParam.put("appId", PayUtils.appId);
//        jsapiPayParam.put("timeStamp", timestamp);
//        jsapiPayParam.put("nonceStr", nonce);//随机字符串
//        jsapiPayParam.put("package", "prepay_id=" + prepay_id);
////        jsapiPayParam.put("signType", "HMAC-SHA256");
////        String paySign=WXPayUtil.generateSignature(jsapiPayParam, PayUtils.apiV3Key, WXPayConstants.SignType.HMACSHA256);
//        jsapiPayParam.put("signType", "RSA");
//        //将h5网页响应给前端
////        jsapiPayParam.put("paySign", this.sign(jsapiPayParam.toString().getBytes()));
//        jsapiPayParam.put("paySign",paySign);
//        System.out.println("1123:"+jsapiPayParam.toString());
//        return new ModelAndView("wxpay", jsapiPayParam);
//
//        }
//
//
//
//    public static PrivateKey getPrivateKey(String filename) throws Exception {
//        InputStream is = WxPayController.class.getClassLoader().getResourceAsStream(filename);
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
//
//
//
//
//
//
//
//
//
////    String sign(byte[] message) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
////        Signature sign = Signature.getInstance("SHA256withRSA");
////        sign.initSign(PayUtils.privateKey2);
////        sign.update(message);
////        return Base64.getEncoder().encodeToString(sign.sign());
////    }
//
//
//
//
////    @GetMapping("/wxjsapipayit")
////    public ModelAndView createOrder(HttpServletRequest request) throws Exception {
////        //部分1，获得prepay_id
////        System.out.println("wxjsapipayit-----------------");
////        String openid = request.getParameter("openid");
////        System.out.println("接受到的openid:"+openid);
////        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(PayUtils.privateKey.getBytes(StandardCharsets.UTF_8)));
////        //使用自动更新的签名验证器，不需要传入证书
////        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
////                new WechatPay2Credentials(PayUtils.merchantId, new PrivateKeySigner(PayUtils.merchantSerialNumber , merchantPrivateKey)),
////                PayUtils.apiV3Key.getBytes(StandardCharsets.UTF_8));
////
////        CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
////                .withMerchant(PayUtils.merchantId, PayUtils.merchantSerialNumber, merchantPrivateKey)
////                .withValidator(new WechatPay2Validator(verifier))
////                .build();
////
////        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi");
////        httpPost.addHeader("Accept", "application/json");
////        httpPost.addHeader("Content-type","application/json; charset=utf-8");
////
////        ByteArrayOutputStream bos = new ByteArrayOutputStream();
////        ObjectMapper objectMapper = new ObjectMapper();
////
////        ObjectNode rootNode = objectMapper.createObjectNode();
////        rootNode.put("mchid", PayUtils.merchantId)
////                .put("appid", PayUtils.appId)
////                .put("description", "Image形象店-深圳腾大-QQ公仔")
////                .put("notify_url", PayUtils.notify_url)
////                .put("out_trade_no", System.currentTimeMillis()+"111168");
////        rootNode.putObject("amount")
////                .put("total", 1);
////        rootNode.putObject("payer")
////                .put("openid", openid);
////
////        objectMapper.writeValue(bos, rootNode);
////
////        httpPost.setEntity(new StringEntity(bos.toString("UTF-8"), "UTF-8"));
////        CloseableHttpResponse response = httpClient.execute(httpPost);
////
////        String bodyAsString = EntityUtils.toString(response.getEntity());
////        System.out.println("bodyAsString::::"+bodyAsString);
////
////
////
////
////
////
////        //部分2,获得签名
////        String timestamp = System.currentTimeMillis() / 1000 + "";
////        String nonce = RandomUtil.randomString(32);
////        StringBuilder builder = new StringBuilder();
////        //应用id
////        builder.append(PayUtils.appId).append("\n");
////        //时间戳
////        builder.append(timestamp).append("\n");
////        //随机字符串
////        builder.append(nonce).append("\n");
////
////        JsonNode node = objectMapper.readTree(bodyAsString);
////        //预支付交易会话ID
////        builder.append(node.get("prepay_id")).append("\n");
////
////        System.out.println("预支付id:::："+node.get("prepay_id"));
////
////        String ciphertext = RsaCryptoUtil.encryptOAEP(builder.toString(), verifier.getValidCertificate());
////        System.out.println("ciphertext::"+ciphertext);
//////        Map map=new HashMap();
//////        map.put("noncesstr",nonce);
//////        map.put("package",PayUtils.package1);
//////        map.put("timestamp",timestamp);
//////        map.put("sign",ciphertext);
//////        System.out.println("map:::::"+map);
////
////
////
////        //准备h5网页需要的数据
////        Map<String, String> jsapiPayParam = new HashMap<>();
////        jsapiPayParam.put("appId", PayUtils.appId);
////        jsapiPayParam.put("timeStamp", timestamp);
////        jsapiPayParam.put("nonceStr", nonce);//随机字符串
////        jsapiPayParam.put("package", "prepay_id=" + node.get("prepay_id"));
////        jsapiPayParam.put("signType", "HMAC-SHA256");
////        //将h5网页响应给前端
////        jsapiPayParam.put("paySign", WXPayUtil.generateSignature(jsapiPayParam, PayUtils.apiV3Key, WXPayConstants.SignType.HMACSHA256));
////        return new ModelAndView("wxpay", jsapiPayParam);
////
////    }
//
//    @PostMapping("/wxcallback") //支付成功回调
//    public Map callback(HttpServletRequest request){
//
//        String Timestamp=request.getHeader("Wechatpay-Timestamp");
//        String Nonce=request.getHeader("Wechatpay-Nonce");
//        String signature=request.getHeader("Wechatpay-Signature");
//        String serialNumber=request.getHeader("Wechatpay-Serial");
//        System.out.println("Wechatpay-Timestamp:"+request.getHeader("Wechatpay-Timestamp"));
//        System.out.println("Wechatpay-Nonce:"+request.getHeader("Wechatpay-Nonce"));
//        System.out.println("Wechatpay-Signature:"+request.getHeader("Wechatpay-Signature"));
//        System.out.println("Wechatpay-Serial:"+request.getHeader("Wechatpay-Serial"));
//
//        Map result=new HashMap();
//        result.put("code","FAIL");
//        try {
//            BufferedReader bufferedReader = request.getReader(); //获得请求体
//            String str=null;
//            StringBuilder builder=new StringBuilder();
//            while ((str=bufferedReader.readLine())!=null){
//                builder.append(builder);
//            }
//            System.out.println("builder::"+builder);
//
//            //验证签名
//            StringBuilder signStr=new StringBuilder();
//            signStr.append(Timestamp).append("\n");
//            signStr.append(Nonce).append("\n");
//            signStr.append(builder.toString()).append("\n");
//            if (!PayUtils.signVerifier(serialNumber,signStr.toString(),signature)){
//                result.put("message","sign error");
//                return result ;
//            }
//            //解密密文
//            System.out.println("被解密的密文：："+PayUtils.decryptOrder(builder.toString()));
//            //验证订单
//            result.put("code","SUCCESS");
//        }catch (IOException e){
//            e.printStackTrace();
//        } catch (GeneralSecurityException e) {
//            throw new RuntimeException(e);
//        }
//        return result;
//    }
//
//
//
//}
