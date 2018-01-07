package com.excel.processor.impl;


import com.excel.generate.generator.Generator;
import com.excel.generate.writer.entity.Bulk;
import com.excel.processor.Processor;
import com.excel.tag.Tag;
import com.excel.tag.analyzer.TagAnalyzer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;

@Service
public class ProcessorImpl implements Processor {

    @Autowired
    TagAnalyzer tagAnalyzerImpl;

    @Autowired
    Generator generatorWithNoRecursionImpl;


    @Override
    public void process(String rawContent) throws Exception {

        Tag tag = tagAnalyzerImpl.analyse(rawContent.trim());

        Bulk bulk = Bulk.get(tag);

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

        bulk.setSheet(sheet);
        bulk.setWorkbook(workbook);

        generatorWithNoRecursionImpl.generate(bulk);

        // 最后一次回车操作会增加一行，删除
        sheet.removeRow(sheet.getRow(sheet.getLastRowNum()));

        System.out.println("主表数据写入完成>>>>>>>>");

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        System.out.println(filePath + "写入文件成功>>>>>>>>>>>");

    }
}
