package com.doll.utils;

import com.doll.dto.ExportOrderDto;
import com.doll.entity.MailOrder;
//import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportFileUtils {


        public static void   writeAddressBookToExcel(List<ExportOrderDto> exportOrderDtoList, String filePath) throws IOException {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Orders");

            int rowNum = 0;
            Row headerRow = sheet.createRow(rowNum++);
            headerRow.createCell(0).setCellValue("订单 ID");
            headerRow.createCell(1).setCellValue("商品 ID");
            headerRow.createCell(2).setCellValue("商品名");
            headerRow.createCell(3).setCellValue("收货人");
            headerRow.createCell(4).setCellValue("收货地址");
            headerRow.createCell(5).setCellValue("详细地址");
            headerRow.createCell(6).setCellValue("备注");
            headerRow.createCell(7).setCellValue("下单时间");

            // 添加其他列标题

            for (ExportOrderDto exportOrderDto : exportOrderDtoList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(exportOrderDto.getMailOrderId().toString());
                row.createCell(1).setCellValue(exportOrderDto.getCommodityId().toString());
                row.createCell(2).setCellValue(exportOrderDto.getCommodityName());
                row.createCell(3).setCellValue(exportOrderDto.getAddressBookConsignee());
                row.createCell(4).setCellValue(exportOrderDto.getAddress());
                row.createCell(5).setCellValue(exportOrderDto.getAddressBookDetail());
                row.createCell(6).setCellValue(exportOrderDto.getMailOrderRemark());
                row.createCell(7).setCellValue(exportOrderDto.getMailOrderCreateTime().toString());
                // 填充其他列数据
            }

            for (int i=0 ;i<headerRow.getLastCellNum();i++){
                sheet.autoSizeColumn(i);
            }


            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                workbook.close();
            }
        }




//    public static <T> void writeToCSV(String filePath, List<T> data, String[] headers) {
//        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
//            // Write the header
//            writer.writeNext(headers);
//
//            // Write the data
//            for (T item : data) {
//                String[] line = convertToStringArray(item);
//                writer.writeNext(line);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private static <T> String[] convertToStringArray(T item) {
//        // Convert item to String array
//        if (item instanceof MailOrder) {
//            MailOrder mailOrder = (MailOrder) item;
//            return new String[] {
//                    mailOrder.getId().toString(),
//                    String.valueOf(mailOrder.getCreateTime()),
//                    mailOrder.getCreateTime().toString().replace("T" ," "),
//                    mailOrder.getRemark()
//            };
//        }
//        return new String[0];
//    }
}
