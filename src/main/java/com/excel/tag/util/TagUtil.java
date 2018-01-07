package com.excel.tag.util;

import com.excel.tag.Tag;
import com.excel.tag.TagRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagUtil {


    /* Tag的attributes 解析时在其后面添加的 =" 组成如 rowspan=" 的形式*/
    public static final String ATTR_APPENDER_PREFIX = "=\"";

    /* Tag的attributes 解析用的末尾endfix */
    public static final String ATTR_APPENDER_ENDFIX = "\"";

    /**
     * 判断tag是否包含attributes
     * 使用{@link TagRepository#AttrMaps}判断是否和当前tag的star一致
     * 例：<td> 是否和 <td rowspan="12">一致 如果不一致说明包含attributes
     *
     * @param tag
     * @return 是否是普通Tag的start形式
     */
    public static boolean isNormalTag(Tag tag) {
        if (TagRepository.AttrMaps.get(tag.getHtmlTagStart()) != null) {
            return true;
        }
        return false;
    }


    /**
     * 从头尾两个方向切掉start与end内容，返回中间内容
     *
     * @param value 需要操作的内容
     * @param start 开始字符串
     * @param end   结束字符串
     * @return
     */
    public static String cutDownFromStartFromEnd(String value, String start, String end) {
        return value.substring(start.length(), value.length() - end.length());
    }

    public static void main(String[] args) throws Exception {
    }


    /**
     * 从start到第一遇到的end位置的所有内容切出
     *
     * @param value 需要操作的内容
     * @param start 开始字符串
     * @param end   结束字符串
     * @param range 是否包含start和end 1:不包含 0:包含
     * @return
     */
    public static String cutDownTagStartUntilMatchFirstEnd(String value, String start, String end, int range) {

        Pattern p = Pattern.compile(start + "(.*?)" + end);
        Matcher m = p.matcher(value);
        if (m.find()) {
            return m.group(range);
        }
        return null;
    }

    /**
     * 返回指定的Attribute的值
     *
     * @param tag
     * @param attribute
     * @return
     */
    public static String getDefaultAttributeValue(Tag tag, String attribute) {
        return cutDownTagStartUntilMatchFirstEnd(
                tag.getHtmlTagStart(),
                attribute.concat(ATTR_APPENDER_PREFIX),
                ATTR_APPENDER_ENDFIX,
                1);
    }
}
