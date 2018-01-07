package com.excel.tag;

import com.excel.Const;
import com.excel.tag.util.TagUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractTag implements Tag {

    private String htmlTagStart;
    private String rawContent;
    private String content;

    private List<Tag> tags = new ArrayList<>();

    private Map<String, String> attributes = new HashMap<>();

    @Override
    public Tag setHtmlTagStart(String tagStart) {
        this.htmlTagStart = tagStart;
        return this;
    }


    /**
     * Tag的html的开始的标示方式，如<td>
     *
     * @return
     */
    @Override
    public String getHtmlTagStart() {
        if (this.htmlTagStart == null) {
            return getRawHtmlTagStart();
        }

        return this.htmlTagStart;
    }

    /**
     * Tag的原始html的开始的标示方式
     *
     * @return
     */
    @Override
    public String getRawHtmlTagStart() {
        return Const.HTML_LEFT_BRACKET
                .concat(getTagName())
                .concat(Const.HTML_RIGHT_BRACKET);
    }

    /**
     * Tag的html的开始的标示方式，如<td
     *
     * @return
     */
    @Override
    public String getHtmlTagStartNoRightBRACKET() {
        return Const.HTML_LEFT_BRACKET
                .concat(getTagName())
                .concat(Const.HTML_TAG_SPACE);
    }

    /**
     * Tag的html的结束的标示方式，如</td>
     *
     * @return
     */
    @Override
    public String getHtmlTagEnd() {
        return Const.HTML_LEFT_BRACKET
                .concat(Const.HTML_TAG_SLASH)
                .concat(getTagName())
                .concat(Const.HTML_RIGHT_BRACKET);
    }


    /**
     * 获取Sub Tag一览
     *
     * @return
     */
    @Override
    public List<Tag> getSubTags() {
        return this.tags;
    }

    /**
     * 添加Sub TagEntity
     *
     * @param tag
     * @return
     */
    @Override
    public Tag addSubTag(Tag tag) {
        tags.add(tag);
        return this;
    }

    /**
     * 添加Tag中的attribute与value <k,v>
     *
     * @param attr
     * @param value
     * @return
     */
    @Override
    public Tag addAttribute(String attr, Object value) {
        this.attributes.put(attr, String.valueOf(value));
        return this;
    }

    /**
     * 获得Tag中所有的属性与内容
     *
     * @return 属性内容<k,v>
     */
    @Override
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    /**
     * 是否拥有Sub TagEntity
     *
     * @return
     */
    @Override
    public boolean hasSubTag() {
        return tags.size() > 0;
    }

    /**
     * tag间的所有内容
     *
     * @param rawContent
     * @return
     */
    @Override
    public Tag setRawContent(String rawContent) {
        if (this.htmlTagStart == null) {
            this.htmlTagStart = getHtmlTagStart();
        }
        this.rawContent = rawContent;
        return this;
    }

    @Override
    public String getRawContent() {
        return this.rawContent;
    }

    @Override
    public Tag setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String getContent() {
        return this.content;
    }


    @Override
    public Tag copy() throws IllegalAccessException, InstantiationException {
        return this.getClass().newInstance();
    }

    /**
     * 判断是否有attributes
     *
     * @return
     */
    @Override
    public boolean hasAttributes() {
        return this.attributes.size() > 0;
    }

}
