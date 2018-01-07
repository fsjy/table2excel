package com.excel.generate.writer.impl;

import com.excel.generate.writer.Writer;
import com.excel.generate.writer.entity.Bulk;
import com.excel.generate.writer.entity.MergedEntity;
import com.excel.generate.writer.entity.RangeEntity;
import com.excel.generate.writer.style.Style;
import com.excel.tag.Tag;
import com.excel.tag.tagEntity.Td;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExcelWriterImpl implements Writer {

    @Autowired
    Style styleImpl;

    public static final ThreadLocal excelThreadLocal = new ThreadLocal();

    Logger log = LoggerFactory.getLogger(ExcelWriterImpl.class);

    @Override
    public void write(Bulk bulk) {

        RangeEntity r = get();

        if (!r.isContinue()) {
            r.colIndexPlus1();
            refresh(r);

            // 递归进行后面的col的判读写出
            write(bulk);
            return;
        }

        Tag tag = bulk.getTag();
        if (tag.hasAttributes()) {

            log.debug("Tag: {} attributes，需要进行合并分析", tag.getHtmlTagStart());

            // TODO 暂时
            if (tag instanceof Td) {
                Td td = (Td) tag;
                MergedEntity mergedEntity = new MergedEntity();

                if (td.getRowspan() != 0) {
                    mergedEntity.setFirstRow(r.getRowIndex());
                    mergedEntity.setLastRow(r.getRowIndex() + td.getRowspan() - 1);
                    mergedEntity.setFirstColumn(r.getColIndex());
                    mergedEntity.setLastColumn(r.getColIndex());
                    log.debug("td的 rowspan 操作，FirstRow：{}, LastRow: {}", mergedEntity.getFirstRow(), mergedEntity.getLastRow());
                }

                if (td.getColspan() != 0) {
                    mergedEntity.setFirstColumn(r.getColIndex());
                    mergedEntity.setLastColumn(r.getColIndex() + td.getColspan());
                    log.debug("td的 colspan 操作，FirstCol：{}, LastCol: {}", mergedEntity.getFirstColumn(), mergedEntity.getLastColumn());
                }

                //单元格范围 参数（int firstRow, int lastRow, int firstCol, int lastCol)
                CellRangeAddress cellRangeAddress =
                        new CellRangeAddress(
                                mergedEntity.getFirstRow(),
                                mergedEntity.getLastRow(),
                                mergedEntity.getFirstColumn(),
                                mergedEntity.getLastColumn());

                //在sheet里增加合并单元格
                bulk.getSheet().addMergedRegion(cellRangeAddress);
                r.addMergedEntity(mergedEntity);
                log.debug("Merged CellRangeAddress内容： {}", cellRangeAddress);

                r.getRow(bulk.getSheet()).createCell(r.getColIndex()).setCellValue(tag.getContent());
                r.colIndexPlus1();
            }
        } else {

            Cell cell = r.getRow(bulk.getSheet()).createCell(r.getColIndex());
            bulk.setCell(cell);
            cell.setCellValue(tag.getContent());

            styleImpl.setStyle(bulk);

            log.debug("书写操作： {},  row: {} , col: {} ", tag.getContent(), r.getRowIndex(), r.getColIndex());
            r.colIndexPlus1();
        }
        refresh(r);
    }


    @Override
    public void writeLn(Bulk bulk) {

        RangeEntity r = get();

        if (r.getColIndex() == 0) {
            log.debug("不需要回车操作");
        } else {
            log.debug("需要回车操作");
            r.rowIndexPlus1();
            log.debug("行数增加到: {}", r.getRowIndex());
            Row row = bulk.getSheet().createRow(r.getRowIndex());
            r.setRow(row);
            log.debug("重新设置row到RangeEntity中，row为 {}", row);
            r.resetCol();
            log.debug("重新设置col，为 {}", r.getColIndex());
            refresh(r);
        }
    }

    private void refresh(RangeEntity rangeEntity) {
        excelThreadLocal.set(rangeEntity);
    }

    private RangeEntity get() {
        RangeEntity rangeEntity = (RangeEntity) excelThreadLocal.get();
        if (rangeEntity == null) {

            log.debug("首次进入，RangeEntity为空，进行init操作");
            return RangeEntity.init();
        }
        return rangeEntity;
    }
}
