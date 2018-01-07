package com.excel.generate.writer.entity;

import com.excel.generate.matcher.AbstractTagMatcher;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RangeEntity {

    Logger log = LoggerFactory.getLogger(RangeEntity.class);

    private int rowIndex;
    private int colIndex;

    private Row row;

    private Map<MergedEntity, MergedEntity> mergedEntites = new HashMap<>();

    public static RangeEntity init() {
        return new RangeEntity();
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    public Map<MergedEntity, MergedEntity> getMergedEntites() {
        return mergedEntites;
    }

    public void addMergedEntity(MergedEntity mergedEntity) {
        this.mergedEntites.put(mergedEntity, mergedEntity);
    }

    public Row getRow(Sheet sheet) {

        if (this.row == null) {
            row = sheet.createRow(rowIndex);
        }
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public void colIndexPlus1() {
        this.colIndex++;
    }

    public void rowIndexPlus1() {
        this.rowIndex++;
    }

    public void resetCol() {
        this.colIndex = 0;
    }

    /**
     * 判断是否可以跳过本次write处理
     *
     * @return
     */
    public boolean isContinue() {

        Iterator iterator = mergedEntites.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            MergedEntity mergedEntity = ((MergedEntity) entry.getValue());

            // 如果当前的行列在某个合并单元格的范围内，则不需要输出
            if (rowIndex >= mergedEntity.getFirstRow()
                && rowIndex <= mergedEntity.getLastRow()
                && colIndex >= mergedEntity.getFirstColumn()
                && colIndex <= mergedEntity.getLastColumn()) {

                return false;
            }

//            if (rowIndex > mergedEntity.getLastRow()) {
//
//                log.debug("现有rowIndex：{} > mergedEntity.getLastRow(): {} 去除掉  mergedEntites中的内容，继续处理", rowIndex, mergedEntity.getLastRow());
//                mergedEntites.remove(entry.getKey());
//                return true;
//            }
//            if (colIndex > mergedEntity.getLastColumn()) {
//                log.debug("现有colIndex：{} > mergedEntity.getLastColumn(): {} 暂时保留 mergedEntites中的内容，继续处理", colIndex, mergedEntity.getLastColumn());
//                return true;
//            } else {
//
//                log.debug("处于rowspan or colspan中，不需要输出内容. rowIndex : {} colIndex : {}", rowIndex, colIndex);
//                return false;
//            }
//        }

        }

        return true;
    }


}
