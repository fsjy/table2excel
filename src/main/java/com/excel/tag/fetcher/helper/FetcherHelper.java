package com.excel.tag.fetcher.helper;

import com.excel.Const;
import com.excel.tag.Tag;
import com.excel.tag.TagRepository;
import com.excel.tag.fetcher.fetchEntitiy.RawTagInfo;
import com.excel.tag.util.TagUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FetcherHelper {


    public static Logger log = LoggerFactory.getLogger(FetcherHelper.class);

    /**
     * 从rawContent中获得第一个系统支持的tag name
     *
     * @param rawContent
     * @return
     */
    public static RawTagInfo getFirst(String rawContent) throws Exception {

        Tag tagger = null;

        // 按照正常的tag start的匹配策略查找
        for (Tag tag : TagRepository.Tags) {
            if (rawContent.startsWith(tag.getHtmlTagStart())) {
                log.debug("采用无attributes的匹配方式匹配tag的start");
                log.debug("Tag 为： {}", tag.getTagName());
                tagger = tag.copy();

                log.debug("Copy tag {} 一份", tagger);
                break;
            }
        }

        // 如果木有发现匹配tag start 则利用<tag + space的策略查找
        if (tagger == null) {
            for (Tag tag : TagRepository.Tags) {
                if (rawContent.startsWith(tag.getHtmlTagStartNoRightBRACKET())) {
                    log.debug("采用 ==>有<== attributes的匹配方式匹配tag的start");
                    log.debug("Tag 为： {}", tag.getTagName());

                    tagger = tag.copy();
                    log.debug("Copy tag {} 一份", tagger);
                    tagger.setHtmlTagStart(
                            TagUtil.cutDownTagStartUntilMatchFirstEnd(
                                    rawContent,
                                    Const.HTML_LEFT_BRACKET,
                                    Const.HTML_RIGHT_BRACKET,
                                    0));
                    log.debug("重新设定Tag start 为： {}", tagger.getHtmlTagStart());

                    bindAttributesByDefinition(tagger);
                    break;
                }
            }
        }

        // 没有找到匹配的html tag start
        if (tagger == null) {
            return new RawTagInfo(null, null, rawContent, rawContent);
        }

        Pattern wordPattern = Pattern.compile(tagger.getHtmlTagEnd());
        Matcher wordMatcher = wordPattern.matcher(rawContent);
        int tagEnd = 0;
        String rawCutContent = null;

        while (wordMatcher.find()) {
            tagEnd++;

            log.debug("截取rawContent到第{}个位置位置", wordMatcher.end());
            rawCutContent = rawContent.substring(0, wordMatcher.end());
            log.debug("截取rawContent的内容为：{}", rawCutContent);

            // 判断tag的start出现的次数与end出现的次数一致表示才找到了真正此tag的结束位置
            if (StringUtils.countOccurrencesOf(
                    rawCutContent,
                    tagger.getHtmlTagStart()) == tagEnd) {

                log.debug("找到{}个{}的标签，寻找处理正常停止", tagEnd, tagger.getHtmlTagEnd());
                break;
            }
        }

//        if (rawContent == null) {
//            log.error("Html有误，tag不封闭。Tag：{}", tagger.getTagName());
//            throw new RuntimeException("Html有误，tag不封闭。Tag： " + tagger.getTagName());
//        }

        RawTagInfo rawTagInfo =
                new RawTagInfo(
                        tagger,
                        tagger.getTagName(),
                        rawCutContent,
                        TagUtil.cutDownTagStartUntilMatchFirstEnd(
                                rawCutContent,
                                tagger.getHtmlTagStart(),
                                tagger.getHtmlTagEnd(),
                                1));

        log.debug(rawTagInfo.toString());

        return rawTagInfo;
    }

    /**
     * 为tag绑定其start中定义的attributes，方法不对外公开，在tag初始化的时候设定
     *
     * @param tag
     */
    private static void bindAttributesByDefinition(Tag tag) {
        if (!TagUtil.isNormalTag(tag)) {
            List<String> list = TagRepository.AttrMaps.get(tag.getRawHtmlTagStart());
            if (list != null) {
                for (String attribute : list) {
                    String value = TagUtil.getDefaultAttributeValue(tag, attribute);
                    if (value != null) {
                        tag.addAttribute(attribute, value);

                        try {
                            Field field = tag.getClass().getDeclaredField(attribute);
                            field.setAccessible(true);

                            switch (field.getType().getName()) {
                                case ("int"):
                                    field.setInt(tag, Integer.valueOf(value));
                                    break;
                                case ("long"):
                                    field.setLong(tag, Long.valueOf(value));
                                    break;
                                case ("boolean"):
                                    field.setBoolean(tag, Boolean.valueOf(value));
                                    break;
                                case ("byte"):
                                    field.setByte(tag, Byte.valueOf(value));
                                    break;
                                case ("double"):
                                    field.setDouble(tag, Double.valueOf(value));
                                    break;
                                case ("float"):
                                    field.setFloat(tag, Float.valueOf(value));
                                    break;
                                case ("short"):
                                    field.setShort(tag, Short.valueOf(value));
                                    break;
                                default:
                                    field.set(tag, value);
                            }
                            field.setAccessible(false);
                        } catch (NoSuchFieldException e) {
                            log.error("Tag: {} 中的attribute: {} 中的参数:{} 无法被反射设定", tag.getHtmlTagStart(), attribute, value);
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            log.error("Tag: {} 中的attribute: {} 中的参数:{} 无法被反射设定", tag.getHtmlTagStart(), attribute, value);
                            e.printStackTrace();
                        }

                        log.info("Tag {} 中加入attribute：{}", tag.getTagName() + " -->" + attribute, value);
                    }
                }
            }
        }
    }


}
