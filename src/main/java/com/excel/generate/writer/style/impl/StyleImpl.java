package com.excel.generate.writer.style.impl;

import com.excel.generate.writer.entity.Bulk;
import com.excel.generate.writer.style.Style;
import com.excel.test.Const;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.stereotype.Service;

@Service
public class StyleImpl implements Style {
    /**
     * 设定cell的style 根据规则
     *
     * @param bulk
     */
    @Override
    public void setStyle(Bulk bulk) {


        if (bulk.getTag().getContent().startsWith("* [x]")) {

            Font font = bulk.getWorkbook().createFont();
            //font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            //font.setColor(Font.COLOR_RED);

            CellStyle style = bulk.getWorkbook().createCellStyle();
            style.setFont(font);
            style.setFillForegroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            bulk.getCell().setCellStyle(style);
        }

        if (bulk.getTag().getRawHtmlTagStart().equals(Const.HTML_TH_START)) {
            CellStyle style = bulk.getWorkbook().createCellStyle();
            style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            bulk.getCell().setCellStyle(style);

        }

    }
}
