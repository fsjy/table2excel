package com.excel.generate.generator;

import com.excel.generate.writer.entity.Bulk;
import com.excel.tag.Tag;

public interface Generator {

    void generate(Bulk bulk);
}
