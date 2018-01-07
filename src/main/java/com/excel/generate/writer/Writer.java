package com.excel.generate.writer;

import com.excel.generate.writer.entity.Bulk;

public interface Writer {

    void write(Bulk bulk);

    void writeLn(Bulk bulk);
}
