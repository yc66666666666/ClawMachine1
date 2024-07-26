package com.doll.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QrCodeUtil {

    public String createQRCode(String data) throws WriterException, IOException {
        String filePath = "QRCode.png"; // 生成的二维码图片文件路径
        int width = 300; // 图片宽度
        int height = 300; // 图片高度
        String fileType = "png"; // 图片格式
        Path path = FileSystems.getDefault().getPath(filePath);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);

        MatrixToImageWriter.writeToPath(bitMatrix, fileType, path);

        return filePath;
    }

    public static void main(String[] args) {
        QrCodeUtil qrCodeUtil = new QrCodeUtil();
        try {
            String qrCodePath = qrCodeUtil.createQRCode("https://upbaby.qzhuanyou.com/getWXOAuth2Code");
            System.out.println("QR Code generated at: " + qrCodePath);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}

