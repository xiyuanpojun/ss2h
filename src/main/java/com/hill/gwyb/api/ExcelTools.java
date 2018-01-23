package com.hill.gwyb.api;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class ExcelTools {
    private ExcelTools() {
    }

    public static String createFile(Map<String, Object> map) throws Exception {
        File file = new File(WebContentHelper.getRootPath() + "/upload/" + map.get("name") + ".xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook();
        List head = (List) map.get("head");
        List body = (List) map.get("body");
        List titleList = (List) body.get(0);
        List detailList = (List) body.get(1);
        for (int i = 0; i < head.size(); i++) {
            Sheet sheet = workbook.createSheet(head.get(i).toString());
            XSSFFont font = workbook.createFont();
            font.setBold(true);
            XSSFCellStyle style = workbook.createCellStyle();
            style.setFont(font);
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            for (int j = 0; j < head.size(); j++) {
                Row row = sheet.createRow(0);
                Cell title;
                List titleNames = (List) titleList.get(i);
                for (int k = 0; k < titleNames.size(); k++) {
                    title = row.createCell(k);
                    title.setCellValue(titleNames.get(k).toString() == null ? "" : titleNames.get(k).toString());
                    title.setCellStyle(style);
                    sheet.setColumnWidth(k, 20 * 256);
                }
            }
            List detail = (List) detailList.get(i);
            for (int j = 0; j < detail.size(); j++) {
                Row nextRow = sheet.createRow(j + 1);
                List item = (List) detail.get(j);
                for (int k = 0; k < item.size(); k++) {
                    Cell nextCell = nextRow.createCell(k);
                    nextCell.setCellValue(item.get(k) == null ? "" : item.get(k).toString());
                }
            }
        }
        FileOutputStream stream = FileUtils.openOutputStream(file);
        workbook.write(stream);
        stream.close();
        return file.getAbsolutePath();
    }
}
