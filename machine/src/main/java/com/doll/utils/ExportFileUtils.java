package com.doll.utils;

import com.aliyuncs.utils.StringUtils;
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
                row.createCell(0).setCellValue(exportOrderDto.getMailOrderId()!=null ? exportOrderDto.getMailOrderId().toString():"无");
                row.createCell(1).setCellValue(exportOrderDto.getCommodityId()!=null ? exportOrderDto.getCommodityId().toString():"无");
                row.createCell(2).setCellValue(StringUtils.isEmpty(exportOrderDto.getCommodityName())? "无":exportOrderDto.getCommodityName() );
                row.createCell(3).setCellValue(StringUtils.isEmpty(exportOrderDto.getAddressBookConsignee())? "无": exportOrderDto.getAddressBookConsignee());
                row.createCell(4).setCellValue(StringUtils.isEmpty(exportOrderDto.getAddress())? "无": exportOrderDto.getAddress());
                row.createCell(5).setCellValue(StringUtils.isEmpty(exportOrderDto.getAddressBookDetail())? "无": exportOrderDto.getAddressBookDetail());
                row.createCell(6).setCellValue(StringUtils.isEmpty(exportOrderDto.getMailOrderRemark()) ? "无":exportOrderDto.getMailOrderRemark());
                row.createCell(7).setCellValue(exportOrderDto.getMailOrderCreateTime()!=null ?  exportOrderDto.getMailOrderCreateTime().toString():"无");
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
