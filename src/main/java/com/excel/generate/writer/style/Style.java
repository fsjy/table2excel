package com.excel.generate.writer.style;

import com.excel.generate.writer.entity.Bulk;

public interface Style {

    /**
     * 设定cell的style 根据规则
     *
     * @param bulk
     */
    void setStyle(Bulk bulk);
}
