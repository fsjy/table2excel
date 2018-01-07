package com.excel.test;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Write2Excel {

    Logger logger = LoggerFactory.getLogger(Write2Excel.class);

    public static void main(String[] args) throws IOException {


        Map<Region, Region> regionMap = new HashMap<>();

        String tableFilePath = "/Users/yl/Downloads/table";
        String filePath = "/Users/yl/Downloads/writeTest.xlsx";
        String sheetName = "writeTest";


        System.out.println("开始写入文件>>>>>>>>>>>>");
        Workbook workbook = null;
        if (filePath.toLowerCase().endsWith("xls")) {//2003
            workbook = new HSSFWorkbook();
        } else if (filePath.toLowerCase().endsWith("xlsx")) {//2007
            workbook = new XSSFWorkbook();
        } else {
//          logger.debug("invalid file name,should be xls or xlsx");
        }

        Sheet sheet = workbook.createSheet(sheetName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(tableFilePath))));
        String line = null;

        int rowIndex = 0;//标识位，用于标识sheet的行号
        int columnIndex = 0;

        Row row = null;
        StringBuffer stringBuffer = null;

        while ((line = reader.readLine()) != null) {

            boolean flag = refreshMap(regionMap, rowIndex, columnIndex);

            if (!flag) {
                columnIndex++;
                continue;

            }

            // CellRangeAddress cellRangeAddress = new CellRangeAddress();

            // 去掉所有的空格
            line = line.replace("\t", "");

            if (line.startsWith("<table") || line.startsWith("<tr")) {
                continue;
            }

            if (line.startsWith("<td") || line.startsWith("<th")) {

                Region region = null;

                if (line.indexOf("rowspan") > 0) {

                    region = new Region();
                    String[] arrRows = line.split("rowspan=\"");
                    region.setFirstRow(rowIndex);

                    region.setLastRow(rowIndex + Integer.valueOf(arrRows[1].substring(0, arrRows[1].indexOf("\""))) - 1);

                    region.setFirstColumn(columnIndex);
                    region.setLastColumn(columnIndex);


                }

                if (line.indexOf("colspan") > 0) {
                    String[] colRows = line.split("colspan=\"");

                    if (region == null) {
                        region = new Region();
                    }
                    region.setFirstColumn(columnIndex);
                    region.setLastColumn(columnIndex + Integer.valueOf(colRows[1].substring(0, colRows[1].indexOf("\""))));

                }

                if (region != null) {
                    regionMap.put(region, region);


                    //单元格范围 参数（int firstRow, int lastRow, int firstCol, int lastCol)
                    CellRangeAddress cellRangeAddress =
                            new CellRangeAddress(
                                    region.getFirstRow(),
                                    region.getLastRow(),
                                    region.getFirstColumn(),
                                    region.getLastColumn());

                    //在sheet里增加合并单元格
                    sheet.addMergedRegion(cellRangeAddress);


                }


                if (line.endsWith("</td>") || line.endsWith("</th>")) {

                    String newline = line.substring(line.indexOf("<"), line.indexOf(">") + 1);

                    line = line.replace(newline, "");

                    newline = line.substring(line.indexOf("<"), line.indexOf(">") + 1);

                    line = line.replace(newline, "");


                    String content = line.replace("<td>", "");
                    content = content.replace("</td>", "");
                    content = content.replace("<th>", "");
                    content = content.replace("</th>", "");

                    if (row == null) {
                        row = sheet.createRow(rowIndex);
                    }

                    Cell cell = row.createCell(columnIndex);
                    cell.setCellValue(content);
                    columnIndex++;
                    continue;

                } else {
                    continue;

                }


            }


            if (line.replace("\t", "").startsWith("</td>")) {


                if (row == null) {
                    row = sheet.createRow(rowIndex);
                }

                Cell cell = row.createCell(columnIndex);
                cell.setCellValue(stringBuffer.toString());
                stringBuffer = null;
                columnIndex++;
                continue;

            }


            if (line.startsWith("</tr>")) {


                rowIndex++;
                //create sheet row
                row = sheet.createRow(rowIndex);
                columnIndex = 0;
                continue;
            }

            // 内容
            if (stringBuffer == null) {
                stringBuffer = new StringBuffer();
            }


            stringBuffer.append(line);

            if (line.startsWith("</table>")) {
                break;
            }


        }


        System.out.println("主表数据写入完成>>>>>>>>");
        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        System.out.println(filePath + "写入文件成功>>>>>>>>>>>");

    }


    public static boolean refreshMap(Map<Region, Region> map, int rowIndex, int columnIndex) {

        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();

            Region region = ((Region) entry.getValue());

//            if (region.getLastRow() < rowIndex) {
//                map.remove(entry.getKey());
//                return true;
//            }
//
//            if (region.getLastRow() == rowIndex && region.getLastColumn() < columnIndex) {
//                map.remove(entry.getKey());
//                return true;
//            }

            if (rowIndex > region.getLastRow()) {
                map.remove(entry.getKey());
                return true;
            }

            if (columnIndex > region.getLastColumn()) {
                return true;
            } else {
                return false;
            }
//
//            if (columnIndex <= region.getLastColumn() && columnIndex >= region.getFirstColumn() &&
//                    rowIndex <= region.getLastRow() && rowIndex >= region.getFirstRow()) {
//                return false;
//            } else {
//
//                map.remove(entry.getKey());
//                return true;
//            }


        }

        return true;


    }
}
