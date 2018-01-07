package com.excel.tag.fetcher.fetchEntitiy;

import com.excel.tag.Tag;

public class RawTagInfo {

    public RawTagInfo(Tag tag, String tagName, String rawContentWithTag, String rawContentWithOutTag) {
        this.tag = tag;
        this.tagName = tagName;
        this.rawContentWithTag = rawContentWithTag;
        this.rawContentWithOutTag = rawContentWithOutTag;
    }

    private Tag tag;

    private String tagName;

    private String rawContentWithTag;

    private String rawContentWithOutTag;

    public Tag getTag() {
        return tag;
    }

    public String getTagName() {
        return tagName;
    }

    public String getRawContentWithTag() {
        return rawContentWithTag;
    }

    public String getRawContentWithOutTag() {
        return rawContentWithOutTag;
    }

    @Override
    public String toString() {
        String sep = java.security.AccessController.doPrivileged(
                new sun.security.action.GetPropertyAction("line.separator"));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("==============RawTagInfo信息如下==============");
        stringBuffer.append(sep);
        stringBuffer.append("tag                  : ");
        if (tag == null) {
            stringBuffer.append(rawContentWithTag);
        } else {
            stringBuffer.append(tag);
        }
        stringBuffer.append(sep);
        stringBuffer.append("tagName              : ");
        if (tag == null) {
            stringBuffer.append("----  无  ----");
        } else {
            stringBuffer.append(this.tagName);
        }
        stringBuffer.append(sep);
        stringBuffer.append("rawContentWithTag    : ");
        stringBuffer.append(rawContentWithTag);
        stringBuffer.append(sep);
        stringBuffer.append("rawContentWithOutTag : ");
        stringBuffer.append(rawContentWithOutTag);
        return stringBuffer.toString();
    }
}
