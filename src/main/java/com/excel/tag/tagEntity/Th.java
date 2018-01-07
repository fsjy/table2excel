package com.excel.tag.tagEntity;

import com.excel.Const;
import com.excel.tag.AbstractTag;
import com.excel.tag.Tag;
import com.excel.tag.meta.TagEntity;

@TagEntity
public class Th extends AbstractTag {

    private String name = Const.HTML_TAG_TH;

    /**
     * Tag的英文名称，如td，tr，table
     *
     * @return
     */
    @Override
    public String getTagName() {
        return this.name;
    }


}
