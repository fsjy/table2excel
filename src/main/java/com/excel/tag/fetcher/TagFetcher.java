package com.excel.tag.fetcher;

import com.excel.tag.fetcher.fetchEntitiy.RawTagInfo;

public interface TagFetcher {

    RawTagInfo fetch(RawTagInfo rawTagInfo) throws Exception;
}
