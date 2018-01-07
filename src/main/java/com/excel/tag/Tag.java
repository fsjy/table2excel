package com.excel.tag;

import java.util.List;
import java.util.Map;

public interface Tag {

    /**
     * Tag的英文名称，如td，tr，table
     *
     * @return
     */
    String getTagName();

    /**
     * Tag的html的开始的标示方式，如<td>
     *
     * @return
     */
    String getHtmlTagStart();

    /**
     * Tag的原始html的开始的标示方式
     *
     * @return
     */
    String getRawHtmlTagStart();

    /**
     * 如果tag的start中还有attributes 则需要重新标注tag start
     *
     * @return
     */
    Tag setHtmlTagStart(String tagStart);

    /**
     * Tag的html的开始的标示方式，如<td
     *
     * @return
     */
    String getHtmlTagStartNoRightBRACKET();


    /**
     * Tag的html的结束的标示方式，如</td>
     *
     * @return
     */
    String getHtmlTagEnd();

    /**
     * 获取Sub Tag一览
     *
     * @return
     */
    List<Tag> getSubTags();

    /**
     * 添加Sub TagEntity
     *
     * @param tag
     * @return
     */
    Tag addSubTag(Tag tag);

    /**
     * 添加Tag中的attribute与value <k,v>
     *
     * @param attr
     * @param value
     * @return
     */
    Tag addAttribute(String attr, Object value);

    /**
     * 获得Tag中所有的属性与内容
     *
     * @return 属性内容<k,v>
     */
    Map<String, String> getAttributes();

    /**
     * 是否拥有Sub TagEntity
     *
     * @return
     */
    boolean hasSubTag();

    /**
     * tag间的所有内容
     *
     * @param rawContent
     * @return
     */
    Tag setRawContent(String rawContent);

    String getRawContent();

    Tag setContent(String content);

    String getContent();

    /**
     * 拷贝一份自己
     *
     * @return
     */
    Tag copy() throws IllegalAccessException, InstantiationException;

    /**
     * 判断是否有attributes
     *
     * @return
     */
    boolean hasAttributes();


}
