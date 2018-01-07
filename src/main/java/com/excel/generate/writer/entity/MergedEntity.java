package com.excel.generate.writer.entity;

public class MergedEntity {

    private int firstRow = -1;
    private int lastRow = -1;
    private int firstColumn = -1;
    private int lastColumn = -1;

    boolean isRowSpan() {
        return firstRow > 0 && lastRow > 0;
    }

    boolean isColSpan() {
        return firstColumn != lastColumn;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getLastRow() {
        return lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

    public int getFirstColumn() {
        return firstColumn;
    }

    public void setFirstColumn(int firstColumn) {
        this.firstColumn = firstColumn;
    }

    public int getLastColumn() {
        return lastColumn;
    }

    public void setLastColumn(int lastColumn) {
        this.lastColumn = lastColumn;
    }
}
