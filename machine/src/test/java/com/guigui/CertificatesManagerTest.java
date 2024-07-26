//package com.guigui;
//
//import static org.apache.http.HttpHeaders.ACCEPT;
//import static org.apache.http.HttpStatus.SC_OK;
//import static org.apache.http.entity.ContentType.APPLICATION_JSON;
//import static org.junit.Assert.assertEquals;
//
//import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
//import com.wechat.pay.contrib.apache.httpclient.WechatPayUploadHttpPost;
//import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
//import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
//import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
//import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
//import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
//import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//import java.security.PrivateKey;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.utils.URIBuilder;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.util.EntityUtils;
//import org.apache.http.HttpHost;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//public class CertificatesManagerTest {
//
//    // 你的商户私钥
//    private static final String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
//            "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCZnEVqoMxtwwyq\n" +
//            "6xh6+KUmBev0xu4HBa0FVlJvyKnu3HzlQhNZ1PQim23lOZRJGhn80+KyGrxChdmT\n" +
//            "+JJf3RJZvIk1+htyfRGtD7hLnrBUQhQylt3t41ZZxUcgnCgAnbSYw5283BBfWVhA\n" +
//            "7RrGC8XTlfl9Odoo3FMaHj0XOfZsJRuP4F7fWzWCm/eBtDi8VRBeYyEzn48T4K09\n" +
//            "a7c6oR5764ZYm8Aj6jRR1B1UEZTb/bvMmOIuJW7q1KXK/LrgoFepyngRUjoIBcoq\n" +
//            "xsvsLqYSJ7onSp6U4dTKk68mysDmkpBItLlOH+c9lOioPVKcd0hp6QXq7qL0Mnop\n" +
//            "4QQLQ3MfAgMBAAECggEAR3Up2aN51emxf+UjmzkF4n1IubK80elzBpOUf8nSO3q4\n" +
//            "YxHs3JIzC9JQOh0h/Su2QSX0RDQptax8fdhZh1zZB4OsNL7ne8fwrBgu/IaSsm+W\n" +
//            "473I+bwS1GnwX5wlA7xZYeNGArBAsYpngrSKCu05Fueiiv2Znf54biL4+lANcVrN\n" +
//            "SdDCrxA9KU7sueD65XkofjelXCaH5OstRKmsDaxtaPpdMhcMQ7Up9inwOr8nbQs0\n" +
//            "g3lGjZzAzNPMJe92qXZZiAgnitG78nhXwCAKy7Jy7tzJ9SM3H4Y2olJahtBs4wzT\n" +
//            "YH7SVRdjXXAY0gf0d1nR/aJKzbcfJoHTb8SzBpGzQQKBgQDHhBVMuqNVftjdv7oa\n" +
//            "BuMm3ax7UWuxj7lwr4ulWsBKUhGVVAtRQ/uWFnui7dj7GaaG4RXSeYelaY3Fk7eD\n" +
//            "ttoW/8S2kQILfJZ6a7GwQgIGD1c63lrc9i8aAEcZ5zyK8MW3Rp4hIZwOAe48jCaM\n" +
//            "VFP0Xt6EZOcFKJUVmaST/Lb0oQKBgQDFGS+jR26kitpI1UR4FTZpPledMcPqc8dK\n" +
//            "ZNKew+daQIq/aa3YSwHgD8ysPP80nYf9HbYMPzTlfuuxWrerlqiT28+h2mK/5TjD\n" +
//            "OgqySMiaUtckCPu+ve6oab3tYcpi58m4bM3pSA0cYIbjLSMFiLNGi5jZPSCftGFl\n" +
//            "hiq66f2PvwKBgG8pFjzz18DwtZ+HDQcnQzQKWekzSg9aBnXUOLfeQjimVQmgE9Yp\n" +
//            "TGkpCaWTY0sz1WZczi3AbGUq1ER6ssp9/DDIPC+Dx2Hi/mwiyJNtk3cQ4Qb5kCKA\n" +
//            "P2n7MaIrBb5bu60LeVr4QNxFExlE9M1Gxqfmm4+kMFrda4LnYZzaNo1BAoGBALdx\n" +
//            "0qHQyU0CI1pojLqLW8ImkRswh10+dz1pi5LTWQ/qRHkVXgWTAmLNsbyZZRpOiJRw\n" +
//            "Q9EwPwZe0/WVLp/YsHw1uRG2lfRu3x7tdaksDvF7qtvSNKT6WBeNm4VGYD0f6OLq\n" +
//            "+ddnBTTlLoZgq1jqO5msMg33U9bw5zm73ydqw2I3AoGBALpSa31D4Ibr3i1h5qyW\n" +
//            "X3BlKZwIrAfzskXf0/W1yF5YncXgkGhZ4j/LfKjqrbN+vbZ/P7mhV/QtpgXTcXgu\n" +
//            "kcbiHb4vvPFIpCcAgBnd5JmKgVMKFiDGPOwwXrXm1BwV4GCkKEmMD19+XGUJiGI4\n" +
//            "ub8sNlqzhbPvVnyZ5qOA1KVd\n" +
//            "-----END PRIVATE KEY-----";
//    private static final String merchantId = ""; // 商户号
//    private static final String merchantSerialNumber = ""; // 商户证书序列号
//    private static final String apiV3Key = ""; // API V3密钥
//    private CloseableHttpClient httpClient;
//    CertificatesManager certificatesManager;
//    Verifier verifier;
//
//    private static final HttpHost proxy = null;
//
//    @Before
//    public void setup() throws Exception {
//        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(privateKey);
//        // 获取证书管理器实例
//        certificatesManager = CertificatesManager.getInstance();
//        // 添加代理服务器
//        certificatesManager.setProxy(proxy);
//        // 向证书管理器增加需要自动更新平台证书的商户信息
//        certificatesManager.putMerchant(merchantId, new WechatPay2Credentials(merchantId,
//                        new PrivateKeySigner(merchantSerialNumber, merchantPrivateKey)),
//                apiV3Key.getBytes(StandardCharsets.UTF_8));
//        // 从证书管理器中获取verifier
//        verifier = certificatesManager.getVerifier(merchantId);
//        // 构造httpclient
//        httpClient = WechatPayHttpClientBuilder.create()
//                .withMerchant(merchantId, merchantSerialNumber, merchantPrivateKey)
//                .withValidator(new WechatPay2Validator(verifier))
//                .build();
//    }
//
//    @After
//    public void after() throws IOException {
//        httpClient.close();
//    }
//
//    @Test
//    public void getCertificateTest() throws Exception {
//        URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/certificates");
//        HttpGet httpGet = new HttpGet(uriBuilder.build());
//        httpGet.addHeader(ACCEPT, APPLICATION_JSON.toString());
//        CloseableHttpResponse response = httpClient.execute(httpGet);
//        assertEquals(SC_OK, response.getStatusLine().getStatusCode());
//        try {
//            HttpEntity entity = response.getEntity();
//            // do something useful with the response body
//            // and ensure it is fully consumed
//            EntityUtils.consume(entity);
//        } finally {
//            response.close();
//        }
//    }
//
//    @Test
//    public void uploadImageTest() throws Exception {
//        String filePath = "/your/home/test.png";
//
//        URI uri = new URI("https://api.mch.weixin.qq.com/v3/merchant/media/upload");
//
//        File file = new File(filePath);
//        try (FileInputStream fileIs = new FileInputStream(file)) {
//            String sha256 = DigestUtils.sha256Hex(fileIs);
//            try (InputStream is = new FileInputStream(file)) {
//                WechatPayUploadHttpPost request = new WechatPayUploadHttpPost.Builder(uri)
//                        .withImage(file.getName(), sha256, is)
//                        .build();
//
//                try (CloseableHttpResponse response = httpClient.execute(request)) {
//                    assertEquals(SC_OK, response.getStatusLine().getStatusCode());
//                    HttpEntity entity = response.getEntity();
//                    // do something useful with the response body
//                    // and ensure it is fully consumed
//                    String s = EntityUtils.toString(entity);
//                    System.out.println(s);
//                }
//            }
//        }
//    }
//
//    @Test
//    public void uploadFileTest() throws Exception {
//        String filePath = "/your/home/test.png";
//
//        URI uri = new URI("https://api.mch.weixin.qq.com/v3/merchant/media/upload");
//
//        File file = new File(filePath);
//        try (FileInputStream fileIs = new FileInputStream(file)) {
//            String sha256 = DigestUtils.sha256Hex(fileIs);
//            String meta = String.format("{\"filename\":\"%s\",\"sha256\":\"%s\"}", file.getName(), sha256);
//            try (InputStream is = new FileInputStream(file)) {
//                WechatPayUploadHttpPost request = new WechatPayUploadHttpPost.Builder(uri)
//                        .withFile(file.getName(), meta, is)
//                        .build();
//                try (CloseableHttpResponse response = httpClient.execute(request)) {
//                    assertEquals(SC_OK, response.getStatusLine().getStatusCode());
//                    HttpEntity entity = response.getEntity();
//                    // do something useful with the response body
//                    // and ensure it is fully consumed
//                    String s = EntityUtils.toString(entity);
//                    System.out.println(s);
//                }
//            }
//        }
//    }
//}
