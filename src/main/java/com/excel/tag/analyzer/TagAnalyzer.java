package com.excel.tag.analyzer;


import com.excel.tag.Tag;

public interface TagAnalyzer {

    Tag analyse(String rawContent) throws Exception;
}
