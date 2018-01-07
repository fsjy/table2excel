package com.excel.tag.fetcher.impl;

import com.excel.tag.Tag;
import com.excel.tag.fetcher.TagFetcher;
import com.excel.tag.fetcher.fetchEntitiy.RawTagInfo;
import com.excel.tag.fetcher.helper.FetcherHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 递归html数据并存储到{@link RawTagInfo}中
 */
@Service
public class TagFetcherImpl implements TagFetcher {

    Logger log = LoggerFactory.getLogger(TagFetcherImpl.class);

    @Override
    public RawTagInfo fetch(RawTagInfo rawTagInfo) throws Exception {

        Tag tag = rawTagInfo.getTag();

        if (tag == null) {
            throw new Exception("没有发现Tag!");
        }

        String raw = rawTagInfo.getRawContentWithOutTag();
        RawTagInfo info = FetcherHelper.getFirst(raw);
        String leftRaw = raw;

        while (info.getTag() != null) {
            tag.addSubTag(info.getTag());
            log.debug("在 {} ======> 中加入 ======> {} ", tag.getHtmlTagStart(), info.getTag().getHtmlTagStart());
            log.info("{} add tag ======> {} ", tag.getHtmlTagStart(), info.getTag().getHtmlTagStart());

            // 递归
            fetch(info);
            log.debug("解析『 {} 』中去掉『 {}　』", leftRaw, info.getRawContentWithTag());
            leftRaw = leftRaw.substring(info.getRawContentWithTag().length());
            log.debug("剩余为： {}", leftRaw);
            if ("".equals(leftRaw)) {
                break;
            }
            info = FetcherHelper.getFirst(leftRaw);
        }

        tag.setRawContent(raw);
        tag.setContent(raw);
        log.debug("Tag {} 中已没有可处理的内容，设定 {} ====> Content", tag.getHtmlTagStart(), raw);
        log.info("    {} add content ====> {} ", tag.getHtmlTagStart(), raw);

        return rawTagInfo;

    }
}
