package com.excel.tag.analyzer.impl;

import com.excel.tag.Tag;
import com.excel.tag.TagRepository;
import com.excel.tag.analyzer.TagAnalyzer;
import com.excel.tag.fetcher.TagFetcher;
import com.excel.tag.fetcher.fetchEntitiy.RawTagInfo;
import com.excel.tag.fetcher.helper.FetcherHelper;
import com.excel.tag.fetcher.impl.TagFetcherImpl;
import com.excel.tag.tagEntity.Table;
import com.excel.tag.tagEntity.Td;
import com.excel.tag.tagEntity.Th;
import com.excel.tag.tagEntity.Tr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 分析html内容，解析出Tag结构数据
 */
@Service
public class TagAnalyzerImpl implements TagAnalyzer {


    @Autowired
    TagFetcher tagFetcherImpl;

    @Override
    public Tag analyse(String rawContent) throws Exception {
        RawTagInfo rawTagInfo = tagFetcherImpl.fetch(FetcherHelper.getFirst(rawContent));
        return rawTagInfo.getTag();
    }


    public static void main(String[] args) throws Exception {

        TagAnalyzerImpl tagAnalyzer = new TagAnalyzerImpl();

        tagAnalyzer.tagFetcherImpl = new TagFetcherImpl();

        TagRepository.Tags = new ArrayList<>();

        TagRepository.Tags.add(new Td());
        TagRepository.Tags.add(new Tr());
        TagRepository.Tags.add(new Th());
        TagRepository.Tags.add(new Table());
        String tableFilePath = "/Users/yl/Downloads/table";


        TagRepository.AttrMaps = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("rowspan");
        list.add("colspan");
        TagRepository.AttrMaps.put("<td>", list);

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(tableFilePath))));

        String line = null;


        StringBuffer stringBuffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line.replace("\t", ""));
        }

        tagAnalyzer.analyse(stringBuffer.toString());


    }

}

