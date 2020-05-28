package com.example.recyclerview_search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializeContent implements Serializable {

    private static final long serialVersionID = 1L;

    public static final String CONTENT = "NOTICE_CONTENT";
    private List<TestItems> mNoticeList = null;

    public SerializeContent(){
        mNoticeList = new ArrayList<>();
    }

    public List<TestItems> getList() {
        return mNoticeList;
    }

    public void setList(List<TestItems> noticeList) {
        mNoticeList = noticeList;
    }
}
