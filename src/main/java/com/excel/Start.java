package com.excel;

import com.excel.processor.Processor;
import com.excel.tookit.C;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


/**
 * excel2html的主执行class
 *
 * @author darcula
 */

public class Start {

    public static void main(String[] args) throws Exception {

        String tableFilePath = "/Users/yl/Downloads/table2";

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(tableFilePath))));

        String line = null;


        StringBuffer stringBuffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line.replace("\t", ""));
        }

        // 获得Context
        C c = C.get();

        Processor processor = c.g(Processor.class);

        processor.process(stringBuffer.toString());

    }
}
