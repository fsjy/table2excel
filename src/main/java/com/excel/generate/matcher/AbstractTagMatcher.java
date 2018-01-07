package com.excel.generate.matcher;

import com.excel.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTagMatcher implements TagMatcher {


    Logger log = LoggerFactory.getLogger(AbstractTagMatcher.class);

    /**
     * 判断是否为需要操作的tag
     *
     * @param tag
     * @return
     */
    @Override
    public boolean match(Tag tag) {

        for (String s : getMatchRawTagHtmlStart()) {
            if (tag.getRawHtmlTagStart().equals(s)) {
                log.debug("Tag {} 与需要打印的标签相符合", s);
                return true;
            }
        }
        return false;
    }
}
